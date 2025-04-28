package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificationUserDTO {
    NotificationDTO notification;
    UserDTO sender;
    UserDTO receiver;
    Boolean isRead;
}
