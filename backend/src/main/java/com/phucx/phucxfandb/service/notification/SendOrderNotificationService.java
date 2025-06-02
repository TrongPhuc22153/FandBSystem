package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.enums.OrderAction;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import org.springframework.security.core.Authentication;

public interface SendOrderNotificationService {

    void sendNotificationToUser(String orderId, RequestNotificationDTO requestNotificationDTO);
    
    void sendNotificationToGroup(String orderId, String topic, RequestNotificationDTO requestNotificationDTO);

    void sendNotificationForOrderAction(Authentication authentication, String orderId, OrderAction action, OrderType type, OrderDTO order);

    void sendPlaceOrderNotification(Authentication authentication, String orderId, OrderType type, String paymentMethod, PaymentStatus paymentStatus);

    void sendPreparingNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order);

    void sendPreparedNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order);

    void sendServedNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order);

    void sendReadyNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order);

    void sendCompleteNotification(String employeeUsername, String orderId, OrderType type, OrderDTO order);

    void sendCancelNotification(Authentication authentication, String orderId, OrderType type, OrderDTO order);
}
