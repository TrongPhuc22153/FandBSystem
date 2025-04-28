package com.phucx.phucxfandb.dto.response;

import lombok.*;

@Value
@Builder
public class NotificationDTO {
    String notificationId;
    String message;
    String title;
    TopicDTO topic;
    String status;
    String picture;
}
