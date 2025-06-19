package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.enums.PaymentStatus;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.Authentication;

@Value
@Builder
public class PlaceOrderNotificationEvent {
    Authentication authentication;
    String orderId;
    OrderType orderType;
    String paymentMethod;
    PaymentStatus paymentStatus;
}
