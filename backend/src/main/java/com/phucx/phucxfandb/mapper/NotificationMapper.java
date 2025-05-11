package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {



    @Mapping(target = "order", source = "order")
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "time", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "notification", ignore = true)
    @Mapping(target = "notificationUsers", ignore = true)
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "title", source = "requestNotificationDTO.title")
    @Mapping(target = "message", source = "requestNotificationDTO.message")
    @Mapping(target = "picture", source = "requestNotificationDTO.picture")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toOrderNotification(RequestNotificationDTO requestNotificationDTO, Order order, Topic topic);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "reservation", source = "reservation")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "time", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "notification", ignore = true)
    @Mapping(target = "notificationUsers", ignore = true)
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "title", source = "requestNotificationDTO.title")
    @Mapping(target = "message", source = "requestNotificationDTO.message")
    @Mapping(target = "picture", source = "requestNotificationDTO.picture")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toReservationNotification(RequestNotificationDTO requestNotificationDTO, Reservation reservation, Topic topic);

}
