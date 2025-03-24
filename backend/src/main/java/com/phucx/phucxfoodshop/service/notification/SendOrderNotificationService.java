package com.phucx.phucxfoodshop.service.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;

public interface SendOrderNotificationService {
    // customer send notification message to employee
    public void sendNotificationToEmployee(OrderNotificationDTO notificationDetail) throws NotFoundException;
    public void sendNotificationToAllEmployees(OrderNotificationDTO notificationDetail);
    // employee send notification message to customer
    public void sendNotificationToCustomer(OrderNotificationDTO notificationDetail, Boolean firstNotification) throws NotFoundException;
    public void sendNotificationToAllCustomers(OrderNotificationDTO notificationDetail);

    // send notification to notification service
    public void sendCustomerOrderNotification(OrderNotificationDTO notification, Boolean firstNotification) throws JsonProcessingException, NotFoundException;
    public void sendEmployeeOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException, NotFoundException;
}
