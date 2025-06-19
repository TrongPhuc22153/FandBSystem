package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.enums.ReservationAction;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.Authentication;

@Value
@Builder
public class ReservationActionNotificationEvent {
    Authentication authentication;
    String reservationId;
    ReservationAction action;
    ReservationDTO reservation;
}
