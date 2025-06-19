package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderNotificationEvent {
    RequestNotificationDTO request;
    String orderId;

}
