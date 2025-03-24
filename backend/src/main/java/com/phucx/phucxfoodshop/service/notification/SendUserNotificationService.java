package com.phucx.phucxfoodshop.service.notification;

import com.phucx.phucxfoodshop.model.UserNotificationDTO;

public interface SendUserNotificationService {
    public void sendNotificationToCustomer(UserNotificationDTO userNotificationDTO);
    public void sendNotificationToEmployee(UserNotificationDTO userNotificationDTO);
    public void sendNotificationToAllEmployees(UserNotificationDTO userNotificationDTO);
    public void sendNotificationToAllCustomers(UserNotificationDTO userNotificationDTO);

    // send notification to notification service
    public void sendCustomerNotification(UserNotificationDTO userNotification);
    public void sendEmployeeNotification(UserNotificationDTO userNotification);
}
