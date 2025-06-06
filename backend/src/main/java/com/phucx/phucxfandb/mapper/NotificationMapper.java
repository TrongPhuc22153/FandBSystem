package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.response.NotificationDTO;
import com.phucx.phucxfandb.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Named("toNotificationDTO")
    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "reservationId", source = "reservation.reservationId")
    NotificationDTO toNotificationDTO(Notification notification);

    @Named("toOrderNotification")
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
    Notification toOrderNotification(RequestNotificationDTO requestNotificationDTO, Order order, Topic topic);

    @Named("toReservationNotification")
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
    Notification toReservationNotification(RequestNotificationDTO requestNotificationDTO, Reservation reservation, Topic topic);

}
