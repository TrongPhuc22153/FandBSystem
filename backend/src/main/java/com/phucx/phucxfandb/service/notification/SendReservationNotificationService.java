package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.constant.ReservationAction;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import org.springframework.security.core.Authentication;

public interface SendReservationNotificationService {

    void sendNotificationToUser(String reservationId, RequestNotificationDTO requestNotificationDTO);

    void sendNotificationToGroup(String reservationId, String topic, RequestNotificationDTO requestNotificationDTO);

    void sendNotificationForReservationAction(Authentication authentication, String reservationId, ReservationAction action, ReservationDTO reservation);

    void sendPlaceReservationNotification(Authentication authentication, String reservationId, ReservationDTO reservation);

    void sendPreparingNotification(String employeeUsername, String reservationId, ReservationDTO reservation);

    void sendReadyNotification(String employeeUsername, String reservationId, ReservationDTO reservation);

    void sendCompleteNotification(String employeeUsername, String reservationId, ReservationDTO reservation);

    void sendCancelNotification(Authentication authentication, String reservationId, ReservationDTO reservation);
}
