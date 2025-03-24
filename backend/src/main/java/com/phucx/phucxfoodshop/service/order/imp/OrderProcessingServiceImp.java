package com.phucx.phucxfoodshop.service.order.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.annotations.LoggerAspect;
import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.NotificationTopic;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.exceptions.InSufficientInventoryException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.model.OrderWithProducts;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.notification.SendOrderNotificationService;
import com.phucx.phucxfoodshop.service.order.OrderProcessingService;
import com.phucx.phucxfoodshop.service.order.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderProcessingServiceImp implements OrderProcessingService{
    @Autowired
    private SendOrderNotificationService sendOrderNotificationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;

    @Override
    public void processingOrder(OrderWithProducts order) {
        log.info("processingOrder(order={})", order);
        OrderNotificationDTO notification = new OrderNotificationDTO();
        notification.setTitle(NotificationTitle.CONFIRM_ORDER);
        notification.setTopic(NotificationTopic.Order);
        try {
            notification = processOrder(notification, order);
            // send confirmation notification to customer
            sendOrderNotificationService.sendCustomerOrderNotification(notification, false);
        } catch (NotFoundException | JsonProcessingException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
    

    // process order
    private OrderNotificationDTO processOrder(OrderNotificationDTO notification, OrderWithProducts order
    ) throws JsonProcessingException, NotFoundException{
        CustomerDetail user = customerService.getCustomerByID(order.getCustomerID());
        try {
            notification.setOrderID(order.getOrderID());
            notification = this.validateOrder(order, notification, user);
        } catch (RuntimeException | NotFoundException e) {
            log.warn("Error: {}", e.getMessage());
            exceptionHandler(order, user.getUserID(), "Order #" + order.getOrderID() + 
                " has been canceled due to " + e.getMessage(), notification);
        } catch (InvalidDiscountException e){
            log.warn("Error: Discount is invalid {}", e.getMessage());
            exceptionHandler(order, user.getUserID(), "Order #" + order.getOrderID() + 
                " has been canceled due to invalid discount", notification);
        } catch(InvalidOrderException e){
            log.warn("Error: Order is invalid {}", e.getMessage()); 
            exceptionHandler(order, user.getUserID(), "Order #" + order.getOrderID() + 
                " has been canceled due to invalid order", notification);
        } catch (InSufficientInventoryException e){
            log.warn("Error: Order is invalid due to {}", e.getMessage());
            exceptionHandler(order, user.getUserID(), e.getMessage(), notification);
        }
        return notification;
    }

    // validate order product's stocks
    @LoggerAspect
    private OrderNotificationDTO validateOrder(OrderWithProducts order, OrderNotificationDTO notification, CustomerDetail customer) 
        throws JsonProcessingException, InvalidDiscountException, InvalidOrderException, NotFoundException, 
        InSufficientInventoryException{

        // Notification notification = new Notification();
        notification.setTitle(NotificationTitle.CONFIRM_ORDER);
        notification.setTopic(NotificationTopic.Order);
        // check if the order is pending
        if(!orderService.isPendingOrder(order.getOrderID())){
            throw new InvalidOrderException("Order " + order.getOrderID() + 
                " is not a pending order");
        }
        // chech order employeeid and customerid
        if(order.getEmployeeID()==null || order.getCustomerID()==null){
            throw new InvalidOrderException("Order " + order.getOrderID() + 
                " is invalid due to missing customer or employee information");
        }
        // update employeeID for order
        boolean employeeUpdateCheck = orderService.updateOrderEmployee(order.getOrderID(), order.getEmployeeID());
        if(employeeUpdateCheck){
            // validate and update product instock
            ResponseFormat check = orderService.validateAndProcessOrder(order);
            if(!check.getStatus()){
                throw new RuntimeException(check.getError());
            }
            // notification
            notification.setMessage("Order #"+ order.getOrderID() +" has been confirmed");
            notification.setStatus(NotificationStatus.SUCCESSFUL);
            notification.setReceiverID(customer.getUserID());
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Confirmed);
        } else {
            log.error("Can not update employeeID for order " + order.getOrderID());
            throw new RuntimeException("Can not update employeeID for order "+ order.getOrderID());
        }
        return notification;
    }

    // handle exception
    private void exceptionHandler(OrderWithProducts order, String userID, String errorMessage, OrderNotificationDTO notification) 
        throws JsonProcessingException{
        
        try {
            // notification
            notification.setTitle(NotificationTitle.CANCEL_ORDER);
            notification.setMessage(errorMessage);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setReceiverID(userID);
            // update order status
            orderService.updateOrderStatus(order.getOrderID(), OrderStatus.Canceled);
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
