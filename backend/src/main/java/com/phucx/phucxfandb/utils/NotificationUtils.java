package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.enums.NotificationTopic;
import com.phucx.phucxfandb.enums.ReceiverType;
import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.enums.SenderType;

public class NotificationUtils {

    public static RequestNotificationDTO createRequestNotificationDTOForGroup(String senderUsername, RoleName receiverRole, NotificationTopic topic, String title, String message){
        return RequestNotificationDTO.builder()
                .senderType(SenderType.USER)
                .senderUsername(senderUsername)
                .receiverType(ReceiverType.GROUP)
                .receiverRole(receiverRole)
                .topic(topic)
                .title(title)
                .message(message)
                .build();
    }

    public static RequestNotificationDTO createRequestNotificationDTO(String senderUsername, String receiverUsername, NotificationTopic topic, String title, String message){
        return RequestNotificationDTO.builder()
                .senderType(SenderType.USER)
                .senderUsername(senderUsername)
                .receiverType(ReceiverType.INDIVIDUAL)
                .receiverUsername(receiverUsername)
                .topic(topic)
                .title(title)
                .message(message)
                .build();
    }

    public static RequestNotificationDTO createSystemRequestNotificationDTO(String receiverUsername, NotificationTopic topic, String title, String message){
        return RequestNotificationDTO.builder()
                .senderType(SenderType.SYSTEM)
                .receiverType(ReceiverType.INDIVIDUAL)
                .receiverUsername(receiverUsername)
                .title(title)
                .message(message)
                .topic(topic)
                .build();
    }

}
