package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.enums.OrderAction;
import com.phucx.phucxfandb.enums.OrderType;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.Authentication;

@Value
@Builder
public class OrderActionNotificationEvent {
    Authentication authentication;
    String orderId;
    OrderAction action;
    OrderType type;
    OrderDTO order;
}
