package com.phucx.phucxfandb.service.notification.imps;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.service.notification.NotificationUpdateService;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.phucx.phucxfandb.constant.WebSocketEndpoint.*;

@Service
@RequiredArgsConstructor
public class SendReservationNotificationServiceImpl implements SendReservationNotificationService {
    private final NotificationUpdateService notificationUpdateService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public void sendNotificationToUser(String reservationId, RequestNotificationDTO requestNotificationDTO) {
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), reservationId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSendToUser(
                requestNotificationDTO.getReceiverUsername(),
                QUEUE_MESSAGES,
                notificationDTO
        );
    }

    @Override
    public void sendNotificationToGroup(String reservationId, String topic, RequestNotificationDTO requestNotificationDTO) {
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), reservationId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSend(
                topic,
                notificationDTO
        );
    }

    @Override
    public void sendNotificationForReservationAction(Authentication authentication, String reservationId,
                                                      ReservationAction action, ReservationDTO reservation) {
        String username = authentication.getName();

        switch (action) {
            case PREPARING -> sendPreparingNotification(username, reservationId, reservation);
            case READY -> sendReadyNotification(username, reservationId, reservation);
            case COMPLETE -> sendCompleteNotification(username, reservationId, reservation);
            case CANCEL -> sendCancelNotification(authentication, reservationId, reservation);
        }
    }

    @Override
    public void sendPlaceReservationNotification(Authentication authentication, String reservationId, ReservationDTO reservation) {
        String username = authentication.getName();

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                username,
                RoleName.EMPLOYEE,
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_PLACED,
                String.format("New reservation #%s has been placed by customer %s",
                        reservationId,
                        username)
        );
        this.sendNotificationToGroup(reservationId, TOPIC_KITCHEN, employeeNotification);

        RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                username,
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_CONFIRMED,
                String.format("Your reservation #%s has been confirmed for %s",
                        reservationId,
                        reservation.getStartTime() != null ? reservation.getStartTime().toString() : "a specified time")
        );
        this.sendNotificationToUser(reservationId, customerNotification);
    }

    @Override
    public void sendPreparingNotification(String employeeUsername, String reservationId, ReservationDTO reservation) {
        RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                reservation.getCustomer().getProfile().getUser().getUsername(),
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_PREPARING,
                String.format("Your reservation #%s is now being preparing by our staff", reservationId)
        );

        this.sendNotificationToUser(reservationId, customerNotification);

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                employeeUsername,
                RoleName.EMPLOYEE,
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_PREPARING,
                String.format("Reservation #%s is now being preparing by %s", reservationId, employeeUsername)
        );

        this.sendNotificationToGroup(reservationId, TOPIC_KITCHEN, employeeNotification);
    }

    @Override
    public void sendReadyNotification(String employeeUsername, String reservationId, ReservationDTO reservation) {
        RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                reservation.getCustomer().getProfile().getUser().getUsername(),
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_READY,
                String.format("Good news! Your reservation #%s is now ready for pickup", reservationId)
        );

        this.sendNotificationToUser(reservationId, customerNotification);

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                employeeUsername,
                RoleName.EMPLOYEE,
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_READY,
                String.format("Reservation #%s is prepared and ready for customer", reservationId)
        );

        this.sendNotificationToGroup(reservationId, TOPIC_EMPLOYEE, employeeNotification);
    }

    @Override
    public void sendCompleteNotification(String employeeUsername, String reservationId, ReservationDTO reservation) {
        RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                reservation.getCustomer().getProfile().getUser().getUsername(),
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_COMPLETED,
                String.format("Your reservation #%s has been successfully completed. Thank you for your business!", reservationId)
        );

        this.sendNotificationToUser(reservationId, customerNotification);

        RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                employeeUsername,
                RoleName.EMPLOYEE,
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_COMPLETED,
                String.format("Reservation #%s has been marked as complete by %s", reservationId, employeeUsername)
        );

        this.sendNotificationToGroup(reservationId, TOPIC_EMPLOYEE, employeeNotification);
    }

    @Override
    public void sendCancelNotification(Authentication authentication, String reservationId, ReservationDTO reservation) {
        String username = authentication.getName();
        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE") || a.getAuthority().equals("ROLE_ADMIN"));

        if (isEmployee) {
            RequestNotificationDTO customerNotification = NotificationUtils.createSystemRequestNotificationDTO(
                    reservation.getCustomer().getProfile().getUser().getUsername(),
                    NotificationTopic.RESERVATION,
                    NotificationTitle.RESERVATION_CANCELLED,
                    String.format("Your reservation #%s has been cancelled by our staff. Please contact us for more information.", reservationId)
            );

            this.sendNotificationToUser(reservationId, customerNotification);
        }

        if (!isEmployee) {
            RequestNotificationDTO employeeNotification = NotificationUtils.createRequestNotificationDTOForGroup(
                    username,
                    RoleName.EMPLOYEE,
                    NotificationTopic.RESERVATION,
                    NotificationTitle.RESERVATION_CANCELLED,
                    String.format("Reservation #%s has been cancelled by customer %s", reservationId, username)
            );

            this.sendNotificationToGroup(reservationId, TOPIC_EMPLOYEE, employeeNotification);
        }
    }
}
