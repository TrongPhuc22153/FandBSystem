package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;

public interface SendOrderNotificationService {
    void sendOrderNotificationToTopic(String orderId, RequestNotificationDTO requestNotificationDTO);
    void sendOrderNotificationToEmployee(String orderId, RequestNotificationDTO requestNotificationDTO);
    void sendOrderNotificationToCustomer(String orderId, RequestNotificationDTO requestNotificationDTO);
    void sendOrderNotificationToKitchen(String orderId, RequestNotificationDTO requestNotificationDTO);


//    // customer send notification message to employee
//    public void sendNotificationToEmployee(OrderNotificationDTO notificationDetail) throws NotFoundException;
//    public void sendNotificationToAllEmployees(OrderNotificationDTO notificationDetail);
//    // employee send notification message to customer
//    public void sendNotificationToCustomer(OrderNotificationDTO notificationDetail, Boolean firstNotification) throws NotFoundException;
//    public void sendNotificationToAllCustomers(OrderNotificationDTO notificationDetail);
//
//    // send notification to notification service
//    public void sendCustomerOrderNotification(OrderNotificationDTO notification, Boolean firstNotification) throws JsonProcessingException, NotFoundException;
//    public void sendEmployeeOrderNotification(OrderNotificationDTO notification) throws JsonProcessingException, NotFoundException;
}
