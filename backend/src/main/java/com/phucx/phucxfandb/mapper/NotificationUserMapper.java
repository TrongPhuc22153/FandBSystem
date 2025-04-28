package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.entity.Notification;
import com.phucx.phucxfandb.entity.NotificationUser;
import com.phucx.phucxfandb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationUserMapper {

    NotificationUserDTO toNotificationUserDTO(NotificationUser notificationUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "receiver", source = "receiver")
    @Mapping(target = "notification", source = "notification")
    @Mapping(target = "isRead", constant = "false")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    NotificationUser toNotification(RequestNotificationDTO requestNotificationDTO, User sender, User receiver, Notification notification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", source = "receiver")
    @Mapping(target = "notification", source = "notification")
    @Mapping(target = "isRead", constant = "false")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    NotificationUser toNotification(RequestNotificationDTO requestNotificationDTO, User receiver, Notification notification);

}
