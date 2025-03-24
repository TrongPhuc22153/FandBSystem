package com.phucx.phucxfoodshop.service.notification.imps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.MarkNotificationType;
import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.NotificationIsRead;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.NotificationDetail;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.service.notification.MarkUserNotificationService;
import com.phucx.phucxfoodshop.service.notification.NotificationService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarkUserNotificationServiceImp implements MarkUserNotificationService{
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    

    @Override
    public Boolean markAsReadEmployeeNotification(String notificationID, String userID) throws NotFoundException {
        log.info("markAsReadEmployeeNotification(notificationID={}, userID={})", notificationID, userID);
        NotificationDetail notification = this.notificationService.getNotificationByUserIDOrBroadCastAndNotificationID(
            userID, NotificationBroadCast.ALL_EMPLOYEES, notificationID);
        return this.notificationService.updateNotificationReadStatusOfUser(
            notification.getNotificationID(), userID, NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadCustomerNotification(String notificationID, String userID) throws NotFoundException {
        log.info("markAsReadCustomerNotification(notificationID={}, userID={})", notificationID, userID);
        NotificationDetail notification = this.notificationService.getNotificationByUserIDOrBroadCastAndNotificationID(
            userID, NotificationBroadCast.ALL_CUSTOMERS, notificationID);
        return this.notificationService.updateNotificationReadStatusOfUser(
            notification.getNotificationID(), userID, NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadCustomerNotifications(String userID) {
        log.info("markAsReadCustomerNotifications(userID={})", userID);
        return this.notificationService.updateNotificationsReadStatus(
            userID, NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadEmployeeNotifications(String userID) {
        log.info("markAsReadEmployeeNotifications(userID={})", userID);
        return this.notificationService.updateNotificationsReadStatus(
            userID, NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadEmployeeBroadcastNotification(String notifiationID) throws NotFoundException {
        log.info("markAsReadEmployeeBroadcastNotification({})", notifiationID);
        NotificationDetail fetchedNotification = this.notificationService
            .getNotificationByNotificationIDAndReceiverID(
                notifiationID, NotificationBroadCast.ALL_EMPLOYEES.name());
        return notificationService.updateNotificationReadStatusOfBroadcast(
            fetchedNotification.getNotificationID(), 
            NotificationBroadCast.ALL_EMPLOYEES, 
            NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadCustomerBroadcastNotification(String notifiationID) throws NotFoundException {
        log.info("markAsReadCustomerBroadcastNotification({})", notifiationID);
        NotificationDetail fetchedNotification = this.notificationService
            .getNotificationByNotificationIDAndReceiverID(
                notifiationID, NotificationBroadCast.ALL_CUSTOMERS.name());
        return notificationService.updateNotificationReadStatusOfBroadcast(
            fetchedNotification.getNotificationID(), 
            NotificationBroadCast.ALL_CUSTOMERS, 
            NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadForEmployee(String notificationID, String userID, String markType) throws NotFoundException {
        log.info("markAsReadForEmployee(userID={}, markType={})", userID, markType);
        MarkNotificationType type = MarkNotificationType.fromString(markType);
        // mark notification as read based on type
        Boolean status = false;
        if(MarkNotificationType.NOTIFICATION.equals(type)){
            status = this.markAsReadEmployeeNotification(notificationID, userID);
        }else if(MarkNotificationType.ALL.equals(type)){
            status = this.markAsReadEmployeeNotifications(userID);
        }else if(MarkNotificationType.BROADCAST.equals(type)){
            status = this.markAsReadEmployeeBroadcastNotification(notificationID);
        }
        return status;
    }

    @Override
    public Boolean markAsReadForCustomer(String notificationID, String userID, String markType) throws NotFoundException {
        log.info("markAsReadForCustomer(userID={}, marktype)", userID, markType);
        MarkNotificationType type = MarkNotificationType.fromString(markType);
        // mark notification as read based on type
        Boolean status = false;
        if(MarkNotificationType.NOTIFICATION.equals(type)){
            status = this.markAsReadCustomerNotification(notificationID, userID);
        }else if(MarkNotificationType.ALL.equals(type)){
            status = this.markAsReadCustomerNotifications(userID);
        }else if(MarkNotificationType.BROADCAST.equals(type)){
            status = this.markAsReadCustomerBroadcastNotification(notificationID);
        }
        return status;
    }

    @Override
    public Boolean markAsReadForNotification(String notificationID) {
        log.info("markAsReadForNotification({})", notificationID);
        return this.notificationService.updateNotificationReadStatus(
            notificationID, NotificationIsRead.YES.getValue());
    }

    @Override
    public Boolean markAsReadForCustomerByUsername(String notificationID, String username, String markType)
            throws NotFoundException {
        log.info("markAsReadForCustomerByUsername(notificationID={}, username={}, marktype={})", 
            notificationID, username, markType);
        User user = userService.getUser(username);
        return this.markAsReadForCustomer(notificationID, user.getUserID(), markType);
    }

    @Override
    public Boolean markAsReadForEmployeeByUsername(String notificationID, String username, String markType)
            throws NotFoundException {
        log.info("markAsReadForEmployeeByUsername(notificationID={}, username={}, marktype={})", notificationID, username, markType);
        String userid = userService.getUser(username).getUserID();
        return this.markAsReadForEmployee(notificationID, userid, markType);
    }
}
