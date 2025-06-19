package com.phucx.phucxfandb.dto.event;

import com.phucx.phucxfandb.enums.EventAction;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationEvent {
    String id;
    EventAction action;
}
