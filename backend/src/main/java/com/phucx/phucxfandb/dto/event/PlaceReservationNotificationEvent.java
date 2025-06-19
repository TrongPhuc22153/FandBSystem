package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.enums.PaymentStatus;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

@Value
@Builder
public class PlaceReservationNotificationEvent {
    Authentication authentication;
    String reservationId;
    LocalDate reservationDate;
    String paymentMethod;
    PaymentStatus paymentStatus;
}
