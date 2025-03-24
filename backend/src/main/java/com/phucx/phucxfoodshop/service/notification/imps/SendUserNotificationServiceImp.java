package com.phucx.phucxfoodshop.service.notification.imps;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.NotificationIsRead;
import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.constant.WebSocketConstant;
import com.phucx.phucxfoodshop.model.NotificationDetail;
import com.phucx.phucxfoodshop.model.UserNotificationDTO;
import com.phucx.phucxfoodshop.service.messageQueue.MessageQueueService;
import com.phucx.phucxfoodshop.service.notification.SendUserNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendUserNotificationServiceImp implements SendUserNotificationService{
    @Autowired
    private MessageQueueService messageQueueService;

    @Override
    public void sendNotificationToCustomer(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToEmployee({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        // send notification to a specific customer
        messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
    }

    @Override
    public void sendNotificationToEmployee(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToEmployee({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        // send notification to a specific employee
        messageQueueService.sendMessageToUser(notification.getReceiverID(), notification);
    }

    // converter
    private NotificationDetail convertNotification(UserNotificationDTO userNotification){
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(WebConstant.TIME_ZONE));
        NotificationDetail notification = new NotificationDetail(
            userNotification.getTitle().name(), userNotification.getMessage(), 
            userNotification.getSenderID(), userNotification.getReceiverID(), 
            userNotification.getPicture(),userNotification.getTopic().name(), 
            userNotification.getStatus().name(), NotificationIsRead.NO.getValue(), 
            currentDateTime);
        return notification;
    }

    @Override
    public void sendNotificationToAllEmployees(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToAllEmployees({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        messageQueueService.sendNotificationToTopic(notification, 
            WebSocketConstant.TOPIC_EMPLOYEE_NOTIFICAITON_ACCOUNT);
    }

    @Override
    public void sendNotificationToAllCustomers(UserNotificationDTO userNotificationDTO) {
        log.info("sendNotificationToAllCustomers({})", userNotificationDTO);
        NotificationDetail notification = convertNotification(userNotificationDTO);
        messageQueueService.sendNotificationToTopic(notification, 
            WebSocketConstant.TOPIC_CUSTOMER_NOTIFICAITON_ACCOUNT);
    }

    @Override
    public void sendCustomerNotification(UserNotificationDTO userNotification) {
        log.info("sendCustomerNotification({})", userNotification);
        // send message to a user
        if(userNotification.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_CUSTOMERS.name())){
            // notification is send to all customers
            this.sendNotificationToAllCustomers(userNotification);
        }else {
            // send notification to a specific customer
            this.sendNotificationToCustomer(userNotification);
        }
    }

    @Override
    public void sendEmployeeNotification(UserNotificationDTO userNotification) {
        log.info("sendEmployeeNotification({})", userNotification);
        // send message to user
        if(userNotification.getReceiverID().equalsIgnoreCase(NotificationBroadCast.ALL_EMPLOYEES.name())){
            // notification is send to all employees
            this.sendNotificationToAllEmployees(userNotification);
        }else {
            this.sendNotificationToEmployee(userNotification);
        }
    }
    
}
