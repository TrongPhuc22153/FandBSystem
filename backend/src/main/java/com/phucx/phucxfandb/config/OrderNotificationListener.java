package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.dto.event.OrderActionNotificationEvent;
import com.phucx.phucxfandb.dto.event.OrderNotificationEvent;
import com.phucx.phucxfandb.dto.event.PlaceOrderNotificationEvent;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderNotificationListener {

    private final SendOrderNotificationService sendOrderNotificationService;

    @Async
    @EventListener
    public void onNotification(OrderNotificationEvent event) {
        sendOrderNotificationService.sendNotificationToUser(
                event.getOrderId(),
                event.getRequest()
        );
    }

    @Async
    @EventListener
    public void onNotification(PlaceOrderNotificationEvent event) {
        sendOrderNotificationService.sendPlaceOrderNotification(
                event.getAuthentication(),
                event.getOrderId(),
                event.getOrderType(),
                event.getPaymentMethod(),
                event.getPaymentStatus()
        );
    }

    @Async
    @EventListener
    public void onNotification(OrderActionNotificationEvent event) {
        sendOrderNotificationService.sendNotificationForOrderAction(
                event.getAuthentication(),
                event.getOrderId(),
                event.getAction(),
                event.getType(),
                event.getOrder());
    }
}
