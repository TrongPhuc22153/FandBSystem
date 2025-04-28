package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;

public interface NotificationUpdateService {
    NotificationUserDTO createOrderNotification(String username, String orderId, RequestNotificationDTO requestNotificationDTO);
    NotificationUserDTO createReservationNotification(String username, String reservationId, RequestNotificationDTO requestNotificationDTO);
}
