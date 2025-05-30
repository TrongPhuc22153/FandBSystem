package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.ReservationAction;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

public interface SendReservationNotificationService {

    void sendNotificationToUser(String reservationId, RequestNotificationDTO requestNotificationDTO);

    void sendNotificationToGroup(String reservationId, String topic, RequestNotificationDTO requestNotificationDTO);

    void sendNotificationForReservationAction(Authentication authentication, String reservationId, ReservationAction action, ReservationDTO reservation);

    void sendPlaceReservationNotification(Authentication authentication, String reservationId, LocalDateTime reservationStartTime, String paymentMethod, PaymentStatus paymentStatus);

    void sendPreparingNotification(String employeeUsername, String reservationId, ReservationDTO reservation);

    void sendReadyNotification(String employeeUsername, String reservationId, ReservationDTO reservation);

    void sendCompleteNotification(String employeeUsername, String reservationId, ReservationDTO reservation);

    void sendCancelNotification(Authentication authentication, String reservationId, ReservationDTO reservation);
}
