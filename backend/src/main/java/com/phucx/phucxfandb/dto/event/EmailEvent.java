package com.phucx.phucxfandb.dto.event;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailEvent {
    String fromEmail;
    String toEmail;
    String subject;
    String text;
}
