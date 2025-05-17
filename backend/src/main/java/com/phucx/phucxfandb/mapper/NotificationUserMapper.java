package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.entity.Notification;
import com.phucx.phucxfandb.entity.NotificationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NotificationMapper.class, UserMapper.class})
public interface NotificationUserMapper {

    @Mapping(target = "sender", qualifiedByName = "toBriefUserDTO")
    @Mapping(target = "receiver", qualifiedByName = "toBriefUserDTO")
    NotificationUserDTO toNotificationUserDTO(NotificationUser notificationUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(target = "receiverRole", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isRead", constant = "false")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "notification", source = "notification")
    NotificationUser toNotificationUser(RequestNotificationDTO requestNotificationDTO, Notification notification);
}
