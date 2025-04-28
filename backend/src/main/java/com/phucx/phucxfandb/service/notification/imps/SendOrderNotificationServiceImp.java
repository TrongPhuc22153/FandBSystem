package com.phucx.phucxfandb.service.notification.imps;

import com.phucx.phucxfandb.constant.WebSocketEndpoint;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.service.notification.NotificationUpdateService;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.phucx.phucxfandb.constant.WebSocketEndpoint.TOPIC_KITCHEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOrderNotificationServiceImp implements SendOrderNotificationService {
    private final NotificationUpdateService notificationUpdateService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendOrderNotificationToTopic(String orderId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendOrderNotificationToTopic(orderId={}, requestNotificationDTO={})", orderId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createOrderNotification(
                requestNotificationDTO.getSenderUsername(), orderId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSend(
                WebSocketEndpoint.TOPIC_ORDER,
                notificationDTO
        );
    }

    @Override
    public void sendOrderNotificationToEmployee(String orderId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendOrderNotificationToEmployee(orderId={}, requestNotificationDTO={})", orderId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createOrderNotification(
                requestNotificationDTO.getSenderUsername(), orderId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSendToUser(
                requestNotificationDTO.getReceiverUsername(),
                WebSocketEndpoint.QUEUE_MESSAGES,
                notificationDTO
        );

    }

    @Override
    public void sendOrderNotificationToCustomer(String orderId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendOrderNotificationToCustomer(orderId={}, requestNotificationDTO={})", orderId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createOrderNotification(
                requestNotificationDTO.getSenderUsername(), orderId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSendToUser(
                requestNotificationDTO.getReceiverUsername(),
                WebSocketEndpoint.QUEUE_MESSAGES,
                notificationDTO
        );
    }


    @Override
    public void sendOrderNotificationToKitchen(String orderId, RequestNotificationDTO requestNotificationDTO) {
        log.info("sendOrderNotificationToKitchen(orderId={}, requestNotificationDTO={})", orderId, requestNotificationDTO);
        NotificationUserDTO notificationDTO = notificationUpdateService.createReservationNotification(
                requestNotificationDTO.getSenderUsername(), orderId, requestNotificationDTO);
        simpMessagingTemplate.convertAndSend(
                TOPIC_KITCHEN,
                notificationDTO
        );
    }
}   
