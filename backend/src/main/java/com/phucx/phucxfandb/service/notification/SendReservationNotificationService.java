package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;

public interface SendReservationNotificationService {
    void sendReservationNotificationToTopic(String reservationId, RequestNotificationDTO requestNotificationDTO);
    void sendReservationNotificationToEmployee(String reservationId, RequestNotificationDTO requestNotificationDTO);
    void sendReservationNotificationToCustomer(String reservationId, RequestNotificationDTO requestNotificationDTO);
    void sendReservationNotificationToKitchen(String reservationId, RequestNotificationDTO requestNotificationDTO);
}
