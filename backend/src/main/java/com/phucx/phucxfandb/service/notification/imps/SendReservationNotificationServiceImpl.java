package com.phucx.phucxfandb.service.notification.imps;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.service.notification.NotificationUpdateService;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.phucx.phucxfandb.constant.WebSocketEndpoint.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendReservationNotificationServiceImpl implements SendReservationNotificationService {
    private final NotificationUpdateService notificationUpdateService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public void sendReservationNotificationToTopic(String reservationId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendReservationNotificationToTopic(reservationId={}, requestNotificationDTO={})", reservationId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), reservationId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSend(
                TOPIC_RESERVATION,
                notificationDTO
        );
    }

    @Override
    public void sendReservationNotificationToEmployee(String reservationId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendReservationNotificationToEmployee(reservationId={}, requestNotificationDTO={})", reservationId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), reservationId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSendToUser(
                requestNotificationDTO.getReceiverUsername(),
                QUEUE_MESSAGES,
                notificationDTO
        );
    }

    @Override
    public void sendReservationNotificationToCustomer(String reservationId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendReservationNotificationToCustomer(reservationId={}, requestNotificationDTO={})", reservationId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), reservationId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSendToUser(
                requestNotificationDTO.getReceiverUsername(),
                QUEUE_MESSAGES,
                notificationDTO
        );
    }

    @Override
    public void sendReservationNotificationToKitchen(String reservationId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendReservationNotificationToKitchen(reservationId={}, requestNotificationDTO={})", reservationId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), reservationId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSend(
                TOPIC_KITCHEN,
                notificationDTO
        );
    }
}
