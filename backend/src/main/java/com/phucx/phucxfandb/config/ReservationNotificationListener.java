package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.constant.WebSocketEndpoint;
import com.phucx.phucxfandb.dto.event.PlaceReservationNotificationEvent;
import com.phucx.phucxfandb.dto.event.ReservationActionNotificationEvent;
import com.phucx.phucxfandb.dto.event.ReservationNotificationEvent;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationNotificationListener {
    private final SendReservationNotificationService sendReservationNotificationService;

    @Async
    @EventListener
    public void handle(ReservationNotificationEvent event) {
        switch (event.getRequest().getReceiverType()){
            case GROUP -> sendReservationNotificationService.sendNotificationToGroup(
                    event.getReservationId(),
                    WebSocketEndpoint.TOPIC_EMPLOYEE,
                    event.getRequest()
            );
            case INDIVIDUAL -> sendReservationNotificationService.sendNotificationToUser(
                    event.getReservationId(),
                    event.getRequest()
            );
        }
    }

    @Async
    @EventListener
    public void handle(PlaceReservationNotificationEvent event) {
        sendReservationNotificationService.sendPlaceReservationNotification(
                event.getAuthentication(),
                event.getReservationId(),
                event.getReservationDate(),
                event.getPaymentMethod(),
                event.getPaymentStatus()
        );
    }

    @Async
    @EventListener
    public void handle(ReservationActionNotificationEvent event) {
        sendReservationNotificationService.sendNotificationForReservationAction(
                event.getAuthentication(),
                event.getReservationId(),
                event.getAction(),
                event.getReservation()
        );
    }
}
