package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phucx.phucxfandb.constant.ReceiverType;
import com.phucx.phucxfandb.constant.SenderType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationUserDTO {
    String id;

    NotificationDTO notification;

    SenderType senderType;

    UserDTO sender;

    ReceiverType receiverType;

    UserDTO receiver;

    RoleDTO receiverRole;

    Boolean isRead;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
