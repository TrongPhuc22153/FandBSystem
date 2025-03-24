package com.phucx.phucxfoodshop.service.notification.imps;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.NotificationIsRead;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.constant.WebSocketConstant;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.NotificationDetail;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.service.messageQueue.MessageQueueService;
import com.phucx.phucxfoodshop.service.notification.NotificationService;
import com.phucx.phucxfoodshop.service.notification.SendOrderNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendOrderNotificationServiceImp implements SendOrderNotificationService{
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public void sendNotificationToAllEmployees(OrderNotificationDTO orderNotification) {
        log.info("sendNotificationToAllEmployees({})", orderNotification);
        NotificationDetail notificationDetail = this.convertNotification(orderNotification);
        messageQueueService.sendNotificationToTopic(notificationDetail, 
            WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ORDER);
    }

    @Override
    public void sendNotificationToAllCustomers(OrderNotificationDTO orderNotificationDTO) {
        log.info("sendNotificationToAllCustomers({})", orderNotificationDTO);
        
    }
    
    // convert from order notification to notification details
    private NotificationDetail convertNotification(OrderNotificationDTO orderNotification){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(WebConstant.TIME_ZONE));
        NotificationDetail notification = new NotificationDetail(
            orderNotification.getTitle().name(), orderNotification.getMessage(), 
            orderNotification.getSenderID(), orderNotification.getReceiverID(), 
            orderNotification.getPicture(),orderNotification.getTopic().name(), 
            orderNotification.getStatus().name(), NotificationIsRead.NO.getValue(), 
            currentDateTime);
        return notification;
    }

    @Override
    public void sendNotificationToEmployee(OrderNotificationDTO orderNotification) throws NotFoundException {
        log.info("sendNotificationToEmployee({})", orderNotification);
        String orderID = orderNotification.getOrderID();
        if(orderID==null) throw new RuntimeException("Order notification does not contain any orderID");
        NotificationDetail notificationDetail = this.convertNotification(orderNotification);
        log.info("notification: {}", notificationDetail);
        // get the first notification
        if(!(orderNotification.getTitle().equals(NotificationTitle.PLACE_ORDER))){
            NotificationDetail fetchedNotificationDetail = notificationService.getOrderNotificationDetail(
                NotificationTitle.PLACE_ORDER.name(), orderID, NotificationBroadCast.ALL_EMPLOYEES.name());
            // set attribute for the current notification
            notificationDetail.setPicture(fetchedNotificationDetail.getPicture());
            notificationDetail.setRepliedTo(fetchedNotificationDetail.getNotificationID());
        }

        // send notification message to a employee
        messageQueueService.sendMessageToUser(orderNotification.getReceiverID(), notificationDetail);
    }

    @Override
    public void sendNotificationToCustomer(OrderNotificationDTO orderNotification, Boolean firstNotification) throws NotFoundException {
        log.info("sendNotificationToCustomer(orderNotification={}, firstNotification={})", orderNotification, firstNotification);
        // extract notification details
        String orderID = orderNotification.getOrderID();
        if(orderID==null) throw new RuntimeException("Order notification does not contain any orderID");
        NotificationDetail notificationDetail = this.convertNotification(orderNotification);
        log.info("notification: {}", notificationDetail);
        // handle notification which is not the first notification 
        if(!firstNotification){
            // get the first notification aka pending order notification
            if(!(orderNotification.getTitle().equals(NotificationTitle.PLACE_ORDER))){
                NotificationDetail fetchedNotification = notificationService.getOrderNotificationDetail(
                    NotificationTitle.PLACE_ORDER.name(), orderID, orderNotification.getReceiverID());
                // set notification
                notificationDetail.setRepliedTo(fetchedNotification.getNotificationID());
                notificationDetail.setPicture(fetchedNotification.getPicture());
            }
            
            // mark pending order as read when an employee confirm or cancel that order
            if((orderNotification.getTitle().equals(NotificationTitle.CONFIRM_ORDER) ||
                orderNotification.getTitle().equals(NotificationTitle.CANCEL_ORDER))){
                Boolean result = notificationService.updateNotificationReadStatusOfBroadcast(
                    NotificationTitle.PLACE_ORDER.name(), orderID, 
                    NotificationBroadCast.ALL_EMPLOYEES, 
                    NotificationIsRead.YES.getValue());
                if(!result) throw new RuntimeException("Notification with title " + orderNotification.getTitle() + 
                    " and OrderID " + orderID +" can not be updated to " + NotificationIsRead.YES + " status");
            }
        }
        // send notification message to customer
        messageQueueService.sendMessageToUser(orderNotification.getReceiverID(), notificationDetail);
    }
    @Override
    public void sendCustomerOrderNotification(OrderNotificationDTO notification, Boolean firstNotification
    ) throws JsonProcessingException, NotFoundException {
        log.info("sendCustomerOrderNotification(notification={}, firstNotification={})", 
            notification, firstNotification);
        // send message to user
        if(notification.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_CUSTOMERS.name())){
            // send notification message to all customers
            sendNotificationToAllCustomers(notification);
        }else {
            // send notification message to a specific user
            sendNotificationToCustomer(notification, firstNotification);  
        }
    }

    @Override
    public void sendEmployeeOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException, NotFoundException {
        log.info("sendEmployeeOrderNotification({})", notification);
        // send message to user
        if(notification.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_EMPLOYEES.name())){
            // send notification message to all customers
            sendNotificationToAllEmployees(notification);
        }else {
            // send notification message to a specific user
            sendNotificationToEmployee(notification);  
        }
    }
    
}   
