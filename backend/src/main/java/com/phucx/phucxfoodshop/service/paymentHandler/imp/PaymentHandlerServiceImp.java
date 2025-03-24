package com.phucx.phucxfoodshop.service.paymentHandler.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.NotificationTopic;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.notification.SendOrderNotificationService;
import com.phucx.phucxfoodshop.service.order.OrderService;
import com.phucx.phucxfoodshop.service.payment.PaymentManagementService;
import com.phucx.phucxfoodshop.service.paymentHandler.PaymentHandlerService;
import com.phucx.phucxfoodshop.service.product.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentHandlerServiceImp implements PaymentHandlerService{
    @Autowired
    private SendOrderNotificationService sendOrderNotification;
    @Autowired
    private PaymentManagementService paymentManagementService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    
    @Override
    public void paymentSuccessful(String orderId) throws JsonProcessingException, NotFoundException {
        log.info("paymentSuccessful(orderId={})", orderId);
        // update payment status as successful
        paymentManagementService.updatePaymentStatusByOrderID(orderId, PaymentStatusConstant.SUCCESSFUL);
        // get order
        OrderDetails order = orderService.getOrder(orderId);
        CustomerDetail customerDetail = customerService.getCustomerByID(order.getCustomerID());
        String userId = customerDetail.getUserID();
        // get order
        Integer productID = order.getProducts().get(0).getProductID();
        Product fetchedProduct = productService.getProduct(productID);
        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.PLACE_ORDER, 
            userId, NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + orderId + " has been placed successfully");
        customerNotification.setPicture(fetchedProduct.getPicture());
        // send message back to user
        sendOrderNotification.sendCustomerOrderNotification(customerNotification, true);
        // employee
        // send message to notification message queue
        OrderNotificationDTO employeeNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.PLACE_ORDER, 
            userId, NotificationBroadCast.ALL_EMPLOYEES.name(), 
            NotificationTopic.Order, NotificationStatus.SUCCESSFUL);
        employeeNotification.setPicture(fetchedProduct.getPicture());
        employeeNotification.setMessage("A new order #" + orderId + " has been placed");
        sendOrderNotification.sendEmployeeOrderNotification(employeeNotification);
    }

    @Override
    public void paymentFailed(String orderId) throws JsonProcessingException, NotFoundException {
        log.info("paymentFailed(orderId={})", orderId);
        // update payment status as canceled
        paymentManagementService.updatePaymentStatusByOrderID(orderId, PaymentStatusConstant.CANCELLED);
        // get order
        OrderDetails order = orderService.getOrder(orderId);
        CustomerDetail customerDetail = customerService.getCustomerByID(order.getCustomerID());
        String userId = customerDetail.getUserID();
        // update order status
        orderService.updateOrderStatus(orderId, OrderStatus.Canceled);
        // get order
        Integer productID = order.getProducts().get(0).getProductID();
        Product fetchedProduct = productService.getProduct(productID);
        // customer
        // create and save notification back to user
        OrderNotificationDTO customerNotification = new OrderNotificationDTO(
            orderId, NotificationTitle.CANCEL_ORDER, 
            userId, NotificationTopic.Order, 
            NotificationStatus.SUCCESSFUL);
        customerNotification.setMessage("Your order #" + orderId + " has been canceled");
        customerNotification.setPicture(fetchedProduct.getPicture());
        // send message back to user
        sendOrderNotification.sendCustomerOrderNotification(customerNotification, true);

    }    
}
