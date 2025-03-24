package com.phucx.phucxfoodshop.service.order.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTopic;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.EmployeeDetail;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.ProductStockTableType;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.employee.EmployeeService;
import com.phucx.phucxfoodshop.service.notification.SendOrderNotificationService;
import com.phucx.phucxfoodshop.service.order.EmployeeOrderService;
import com.phucx.phucxfoodshop.service.order.OrderProcessingService;
import com.phucx.phucxfoodshop.service.order.OrderService;
import com.phucx.phucxfoodshop.service.payment.PaymentService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeOrderServiceImp implements EmployeeOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private SendOrderNotificationService sendOrderNotification;
    @Autowired
    private OrderProcessingService orderProcessingService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public void confirmOrder(String orderID, String userID) throws InvalidOrderException, NotFoundException, EmployeeNotFoundException, JsonProcessingException, ShipperNotFoundException, CustomerNotFoundException {
        log.info("confirmOrder(orderID={}, userID={})", orderID, userID);
        OrderWithProducts orderWithProducts = orderService.getPendingOrderDetail(orderID);
        // fetch employee
        EmployeeDetail employee = employeeService.getEmployeeByUserID(userID);

        orderWithProducts.setEmployeeID(employee.getEmployeeID());
        // send to order message queue for validating
        orderProcessingService.processingOrder(orderWithProducts);
        
    }

    @Override
    public void cancelPendingOrder(OrderWithProducts order, String userID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, PaymentNotFoundException {
        log.info("cancelPendingOrder(order={}, userID={})", order, userID);
        cancelOrder(order, userID, OrderStatus.Pending);
    }

    @Override
    public void cancelConfirmedOrder(OrderWithProducts order, String userID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, PaymentNotFoundException {
        log.info("cancelConfirmedOrder(order={}, userID={})", order, userID);
        cancelOrder(order, userID, OrderStatus.Confirmed);
        OrderDetails fetchedOrder = orderService.getOrder(order.getOrderID());
        // rollback product instocks
        List<ProductStockTableType> products = fetchedOrder.getProducts().stream()
            .map(product -> new ProductStockTableType(product.getProductID(), product.getQuantity()))
            .collect(Collectors.toList());
        productService.updateProductsInStocks(products);
    }

    // cancel order
    private void cancelOrder(OrderWithProducts order, String userID, OrderStatus orderStatus) 
        throws JsonProcessingException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException, PaymentNotFoundException{

        log.info("cancelOrder(order={}, userID={}, orderStatus={})", order, userID, orderStatus);
        // fetch pending order
        OrderDetails orderDetail = orderService.getOrder(order.getOrderID(), orderStatus);
        // fetch employee
        EmployeeDetail employee = employeeService.getEmployeeByUserID(userID);
        order.setEmployeeID(employee.getEmployeeID());
        Boolean check = orderService.updateOrderEmployee(order.getOrderID(), employee.getEmployeeID());
        if(!check) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated");
        // update order status as canceled
        Boolean status = orderService.updateOrderStatus(orderDetail.getOrderID(), OrderStatus.Canceled);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to canceled status");

        // update payment as canceled
        paymentService.updatePaymentByOrderIDAsCanceled(order.getOrderID());

        // notification
        // set notification details 
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.CANCEL_ORDER);
        notification.setTopic(NotificationTopic.Order);
        if(OrderStatus.Pending.equals(orderStatus) || 
            OrderStatus.Confirmed.equals(orderStatus))
            notification.setOrderID(order.getOrderID());
        if(status){
            // send message to customer
            CustomerDetail fetchedCustomer = customerService.getCustomerByID(order.getCustomerID());
            notification.setReceiverID(fetchedCustomer.getUserID());
            notification.setMessage("Order #" + order.getOrderID() + " has been canceled");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
        }else {
            // send message to employee
            notification.setReceiverID(userID);
            notification.setMessage("Error: Order #" + order.getOrderID() + " can not be canceled");
            notification.setStatus(NotificationStatus.ERROR);
        }
        sendOrderNotification.sendCustomerOrderNotification(notification, false);
    }

    @Override
    public void fullfillOrder(OrderWithProducts order, String userID) throws JsonProcessingException, NotFoundException, CustomerNotFoundException {
        log.info("fullfillOrder(order={}, userID={})", order, userID);
        OrderDetails fetchedOrder = orderService.getOrder(order.getOrderID(), OrderStatus.Confirmed);
        Boolean status = orderService.updateOrderStatus(fetchedOrder.getOrderID(), OrderStatus.Shipping);
        if(!status) throw new RuntimeException("Order #" + order.getOrderID() + " can not be updated to shipping status");
        // notification
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.FULFILL_ORDER);
        notification.setTopic(NotificationTopic.Order);
        notification.setOrderID(order.getOrderID());
        if(status){
            // send message to customer
            CustomerDetail fetchedCustomer = customerService.getCustomerByID(order.getCustomerID());
            notification.setReceiverID(fetchedCustomer.getUserID());
            notification.setMessage("Order #" + order.getOrderID() + " has been fullfilled");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
        }else {
            // send message to employee
            notification.setReceiverID(userID);
            notification.setMessage("Error: Order #" + order.getOrderID() + " can not be fulfilled");
            notification.setStatus(NotificationStatus.ERROR);
        }
        sendOrderNotification.sendCustomerOrderNotification(notification, false);
    }

    @Override
    public Page<OrderDetails> getOrders(String userID, OrderStatus status, int pageNumber, int pageSize)
            throws JsonProcessingException, NotFoundException, EmployeeNotFoundException {
        log.info("getOrders(userID={}, status={}, pageNumber={}, pageSize={})", userID, status, pageNumber, pageSize);
        EmployeeDetail fetchedEmployee = employeeService.getEmployeeByUserID(userID);
        Page<OrderDetails> orders = null;
        if(status.equals(OrderStatus.All)){
            orders = orderService.getOrdersByEmployeeID(
                fetchedEmployee.getEmployeeID(), 
                pageNumber, pageSize);
        }else if(status.equals(OrderStatus.Pending)){
            orders = orderService.getOrders(OrderStatus.Pending, pageNumber, pageSize);
        }else{
            orders = orderService.getOrdersByEmployeeID(
                fetchedEmployee.getEmployeeID(), 
                status, pageNumber, pageSize);
        }
        return orders;
    }

    @Override
    public OrderWithProducts getOrder(String orderID, String userID, OrderStatus status) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException {
        log.info("getOrder(orderID={}, userID={}, orderStatus={})", orderID, userID, status);
        EmployeeDetail fetchedEmployee = employeeService.getEmployeeByUserID(userID);
        OrderWithProducts order = null;
        if(status.equals(OrderStatus.Pending)){
            order = orderService.getPendingOrderDetail(orderID);
        }else if(status.equals(OrderStatus.All)){
            order = orderService.getOrderByEmployeeID(fetchedEmployee.getEmployeeID(), orderID);
        }else {
            order = orderService.getOrderByEmployeeID(fetchedEmployee.getEmployeeID(), orderID, status);
        }
        return order;
    }

    @Override
    public void confirmOrderByUsername(String orderID, String username)
            throws InvalidOrderException, EmployeeNotFoundException, NotFoundException, JsonProcessingException,
            EmployeeNotFoundException, ShipperNotFoundException, CustomerNotFoundException {
        log.info("confirmOrderByUsername(orderid={}, username={})", orderID, username);
        String userid = userService.getUser(username).getUserID();
        this.confirmOrder(orderID, userid);
    }

    @Override
    public void cancelPendingOrderByUsername(OrderWithProducts order, String username) throws JsonProcessingException,
            PaymentNotFoundException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException {
        log.info("cancelPendingOrderByUsername(order={}, username={})", order, username);
        String userid = userService.getUser(username).getUserID();
        this.cancelPendingOrder(order, userid);
    }

    @Override
    public void cancelConfirmedOrderByUsername(OrderWithProducts order, String username) throws JsonProcessingException,
            PaymentNotFoundException, NotFoundException, CustomerNotFoundException, EmployeeNotFoundException {
        log.info("cancelConfirmedOrderByUsername(order={}, username={})", order, username);
        String userid = userService.getUser(username).getUserID();
        this.cancelConfirmedOrder(order, userid);
    }

    @Override
    public void fullfillOrderByUsername(OrderWithProducts order, String username)
            throws JsonProcessingException, PaymentNotFoundException, NotFoundException, CustomerNotFoundException {
        log.info("fullfillOrderByUsername(order={}, username={})", order, username);
        String userid = userService.getUser(username).getUserID();
        this.fullfillOrder(order, userid);
    }

    @Override
    public Page<OrderDetails> getOrdersByUsername(String username, OrderStatus status, int pageNumber, int pageSize)
            throws JsonProcessingException, NotFoundException, EmployeeNotFoundException {
        log.info("getOrdersByUsername(username={}, status={}, pagenumber={}, pagesize={})", username, status, pageNumber, pageSize);
        String userid = userService.getUser(username).getUserID();
        return this.getOrders(userid, status, pageNumber, pageSize);
    }

    @Override
    public OrderWithProducts getOrderByUsername(String orderID, String username, OrderStatus status)
            throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, EmployeeNotFoundException,
            ShipperNotFoundException, CustomerNotFoundException {
        log.info("getOrderByUsername(orderID={}, username={}, status={})", orderID, username, status);
        String userid = userService.getUser(username).getUserID();
        return this.getOrder(orderID, userid, status);
    }
}
