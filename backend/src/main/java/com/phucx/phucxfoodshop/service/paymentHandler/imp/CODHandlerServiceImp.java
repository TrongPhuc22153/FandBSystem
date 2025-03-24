package com.phucx.phucxfoodshop.service.paymentHandler.imp;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.NotificationTopic;
import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.OrderDetails;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.model.Payment;
import com.phucx.phucxfoodshop.model.PaymentDTO;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.notification.SendOrderNotificationService;
import com.phucx.phucxfoodshop.service.order.OrderService;
import com.phucx.phucxfoodshop.service.payment.PaymentManagementService;
import com.phucx.phucxfoodshop.service.paymentHandler.CODHandlerService;
import com.phucx.phucxfoodshop.service.product.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CODHandlerServiceImp implements CODHandlerService{
    @Autowired
    private PaymentManagementService paymentManagementService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SendOrderNotificationService sendOrderNotification;

    @Override
    public Payment createPayment(PaymentDTO paymentDTO) throws JsonProcessingException, NotFoundException {
        log.info("createPayment(paymentDTO={})", paymentDTO);
        // create payment details
        String paymentID = UUID.randomUUID().toString();
        LocalDateTime createdDateTime = LocalDateTime.now();
        String state = PaymentStatusConstant.PENDING.name().toLowerCase();
        String method = paymentDTO.getMethod();

        Payment payment = new Payment(paymentID, createdDateTime, paymentDTO.getAmount(), 
            state, paymentDTO.getCustomerID(), paymentDTO.getOrderID());
        // save payment
        Boolean result = paymentManagementService.savePayment(paymentID, createdDateTime, 
            paymentDTO.getAmount(), state, paymentDTO.getCustomerID(), paymentDTO.getOrderID(), method);

        this.sendNotification(paymentDTO.getOrderID());


        if(!result){
            log.error("Error while saving payment: {}", paymentDTO);
            return null;
        }
        return payment;
    }

    @Override
    public Boolean paymentSuccessfully(String paymentID) {
        log.info("paymentSuccessfully(paymentID={})", paymentID);
        try {
            String state = PaymentStatusConstant.SUCCESSFUL.name().toLowerCase();
            Boolean result = paymentManagementService.updatePaymentStatus(paymentID, state);
            return result;
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } 
    }

    @Override
    public Boolean paymentCancelled(String paymentID) {
        log.info("paymentCancelled(paymentID={})", paymentID);
        try {
            String state = PaymentStatusConstant.FAILED.name().toLowerCase();
            Boolean result = paymentManagementService.updatePaymentStatus(paymentID, state);
            return result;
        } catch (PaymentNotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } 
    }

    // send notification to users
    private void sendNotification(String orderId) throws JsonProcessingException, NotFoundException{
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
    
}
