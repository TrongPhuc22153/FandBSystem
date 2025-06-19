package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.constant.WebSocketEndpoint;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationNotificationEvent {
    String reservationId;
    String endpoint;
    RequestNotificationDTO request;
}
