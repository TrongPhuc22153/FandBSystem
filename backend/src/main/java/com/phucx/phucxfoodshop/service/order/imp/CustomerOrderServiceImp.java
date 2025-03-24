package com.phucx.phucxfoodshop.service.order.imp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTopic;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmailNotVerifiedException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.EmployeeDetail;
import com.phucx.phucxfoodshop.model.InvoiceDetails;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderItem;
import com.phucx.phucxfoodshop.model.OrderItemDiscount;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.PaymentDTO;
import com.phucx.phucxfoodshop.model.PaymentResponse;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.employee.EmployeeService;
import com.phucx.phucxfoodshop.service.notification.SendOrderNotificationService;
import com.phucx.phucxfoodshop.service.order.CustomerOrderService;
import com.phucx.phucxfoodshop.service.order.OrderService;
import com.phucx.phucxfoodshop.service.payment.PaymentService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerOrderServiceImp implements CustomerOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SendOrderNotificationService sendOrderNotification;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;
    @Value("${phucx.redirect-payment-url}")
    private String redirectPaymentUrl;
    
    @Override
    public PaymentResponse placeOrder(OrderWithProducts order, String userID) 
    throws JsonProcessingException, NotFoundException, InvalidDiscountException, InvalidOrderException, CustomerNotFoundException, PayPalRESTException {
        log.info("placeOrder(order={}, userID={})", order, userID);
        User user = userService.getUserById(userID);
        if(!user.getEmailVerified())
            throw new EmailNotVerifiedException("Your email has not verified yet!");

        // fetch customer
        CustomerDetail customer = customerService.getCustomerByUserID(userID);
        order.setCustomerID(customer.getCustomerID());
        // process order
        OrderDetails newOrder = this.orderProcessing(order);
        if(newOrder ==null){
            throw new RuntimeException("Error when placing an order");
        }
        // handle payment based on its method
        BigDecimal totalPrice = newOrder.getTotalPrice()
            .add(newOrder.getFreight());
        PaymentDTO payment = new PaymentDTO(
            totalPrice.doubleValue(), 
            newOrder.getOrderID(), 
            order.getMethod(), 
            order.getCustomerID(), 
            redirectPaymentUrl);
        PaymentResponse paymentResponse = paymentService
            .createPayment(payment);
        return paymentResponse;
    }


    // order processing
    // validating and saving customer's order 
    private OrderDetails orderProcessing(OrderWithProducts order) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException, CustomerNotFoundException {
            
        log.info("orderProcessing({})", order);
        if(order.getCustomerID()==null)
            throw new InvalidOrderException("Customer does not found");
        if(order.getProducts()==null || order.getProducts().isEmpty())
            throw new InvalidOrderException("Your order does not have any products");

        LocalDateTime currenDateTime = LocalDateTime.now();
        order.setOrderDate(currenDateTime);
        // set applied date for discount;
        List<Integer> productIDs = new ArrayList<>();
        for (OrderItem product : order.getProducts()) {
            productIDs.add(product.getProductID());
            for(OrderItemDiscount discount : product.getDiscounts()){
                discount.setAppliedDate(currenDateTime);
            }
        }
        // validate products
        // fetch products
        List<Product> fetchedProducts = productService.getProducts(productIDs);
        // check product and set product's unitprice to customer's order
        for (OrderItem item : order.getProducts()) {
            Product product = this.findProduct(fetchedProducts, item.getProductID())
                .orElseThrow(()-> new NotFoundException("Product " + item.getProductID() + " does not found"));
            item.setUnitPrice(product.getUnitPrice());
        }

        // validate order
        boolean isValidOrder = orderService.validateOrder(order);
        if(!isValidOrder) throw new InvalidOrderException("Order of customer "+order.getCustomerID()+" is not valid");
        // save order
        String pendingOrderID = orderService.saveFullOrder(order);
        order.setOrderID(pendingOrderID);

        OrderDetails orderDetails = orderService.getOrder(pendingOrderID);

        return orderDetails;
    }

    private Optional<Product> findProduct(List<Product> products, Integer productID){
        return products.stream().filter(product -> product.getProductID()==productID).findFirst();
    }

    @Override
    public void receiveOrder(OrderWithProducts order) throws JsonProcessingException, NotFoundException, EmployeeNotFoundException, CustomerNotFoundException, PaymentNotFoundException {
        log.info("receiveOrder(orderID={})", order.getOrderID());
        // get order
        OrderDetails orderDetails = orderService.getOrder(order.getOrderID(), OrderStatus.Shipping);
        // get user 
        EmployeeDetail employeeUser = employeeService.getEmployee(orderDetails.getEmployeeID());
        // update order's status
        Boolean status = orderService.updateOrderStatus(orderDetails.getOrderID(), OrderStatus.Successful);
        // update payment as succcessful
        paymentService.updatePaymentByOrderIDAsSuccesful(orderDetails.getOrderID());
        
        // notification
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.RECEIVE_ORDER);
        notification.setTopic(NotificationTopic.Order);
        notification.setOrderID(orderDetails.getOrderID());
        if(status){
            notification.setMessage("Order #" + orderDetails.getOrderID() + 
                " is received successully by customer " + orderDetails.getCustomerID());
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(employeeUser.getUserID());
        }else {
            notification.setMessage("Order #" + orderDetails.getOrderID() + 
                " can not received by customer " + orderDetails.getCustomerID());
            notification.setStatus(NotificationStatus.ERROR);
            notification.setReceiverID(employeeUser.getUserID());
        }
        sendOrderNotification.sendEmployeeOrderNotification(notification);
    }

    @Override
    public Page<OrderDetails> getOrders(int pageNumber, int pageSize, String userID, OrderStatus orderStatus)
            throws JsonProcessingException, NotFoundException, CustomerNotFoundException, ShipperNotFoundException {
        log.info("getOrders(pageNumber={}, pageSize={}, userID={}, orderStatus={})", pageNumber, pageSize, userID, orderStatus);
        // fetch customer
        CustomerDetail fetchedCustomer = customerService.getCustomerByUserID(userID);
        log.info("customerdetails: {}", fetchedCustomer);
        Page<OrderDetails> orders = null;
        if(orderStatus.equals(OrderStatus.All)){
            orders = orderService.getOrdersByCustomerID(
                fetchedCustomer.getCustomerID(), pageNumber, pageSize);
        }else{
            orders = orderService.getOrdersByCustomerID(
                fetchedCustomer.getCustomerID(), orderStatus, pageNumber, pageSize);
        }
        return orders;
    }

    @Override
    public InvoiceDetails getInvoice(String orderID, String userID) throws JsonProcessingException, ShipperNotFoundException, EmployeeNotFoundException, NotFoundException {
        log.info("getInvoice(orderID={}, userID={})", orderID, userID);
        // fetch customer
        CustomerDetail fetchedCustomer = customerService.getCustomerByUserID(userID);
        InvoiceDetails invoice = orderService.getInvoiceByCustomerID(fetchedCustomer.getCustomerID(), orderID);
        return invoice;
    }

    @Override
    public InvoiceDetails getInvoiceByUsername(String orderID, String username) throws JsonProcessingException, ShipperNotFoundException, EmployeeNotFoundException, NotFoundException{
        log.info("getInvoiceByUsername(orderID={}, username={})", orderID, username);
        User user = userService.getUser(username);
        return this.getInvoice(orderID, user.getUserID());
    }

    @Override
    public PaymentResponse placeOrderByUsername(OrderWithProducts order, String username)
            throws JsonProcessingException, CustomerNotFoundException, InvalidDiscountException, PayPalRESTException,
            InvalidOrderException, NotFoundException {
        log.info("placeOrder(order={}, username={})", order, username);
        String userid = userService.getUser(username).getUserID();
        return this.placeOrder(order, userid);

    }

    @Override
    public Page<OrderDetails> getOrdersByUsername(int pageNumber, int pageSize, String username,
            OrderStatus orderStatus)
            throws JsonProcessingException, NotFoundException, ShipperNotFoundException, CustomerNotFoundException {
        log.info("getOrdersByUsername(pagenumber={}, pagesize={}, username={}, orderstatus={})", pageNumber, pageSize, username, orderStatus);
        String userid = userService.getUser(username).getUserID();
        return this.getOrders(pageNumber, pageSize, userid, orderStatus);
    }
    
}
