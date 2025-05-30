package com.phucx.phucxfandb.service.notification.impl;

import com.phucx.phucxfandb.enums.SenderType;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.request.RequestNotificationUserDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.NotificationMapper;
import com.phucx.phucxfandb.mapper.NotificationUserMapper;
import com.phucx.phucxfandb.repository.NotificationRepository;
import com.phucx.phucxfandb.repository.NotificationUserRepository;
import com.phucx.phucxfandb.service.notification.NotificationUpdateService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import com.phucx.phucxfandb.service.topic.TopicReaderService;
import com.phucx.phucxfandb.service.user.UserReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationUpdateServiceImpl implements NotificationUpdateService {
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationUserMapper notificationUserMapper;
    private final TopicReaderService topicReaderService;
    private final OrderReaderService orderReaderService;
    private final UserReaderService userReaderService;
    private final ReservationReaderService reservationReaderService;
    private final RoleReaderService roleReaderService;

    @Override
    public NotificationUserDTO createOrderNotification(String username, String orderId, RequestNotificationDTO requestNotificationDTO) {
        Topic topic = topicReaderService.getTopicEntity(requestNotificationDTO.getTopic());
        Order order = orderReaderService.getOrderEntity(orderId);

        Notification newNotification = notificationMapper.toOrderNotification(
                requestNotificationDTO,
                order,
                topic
        );

        Notification saved  = this.createNotification(requestNotificationDTO, newNotification);
        NotificationUser notificationUser = saved.getNotificationUsers().get(0);
        return notificationUserMapper.toNotificationUserDTO(notificationUser);
    }

    @Override
    public NotificationUserDTO createReservationNotification(String username, String reservationId, RequestNotificationDTO requestNotificationDTO) {
        Topic topic = topicReaderService.getTopicEntity(requestNotificationDTO.getTopic());
        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);

        Notification newNotification = notificationMapper.toReservationNotification(
                requestNotificationDTO,
                reservation,
                topic
        );

        Notification saved  = this.createNotification(requestNotificationDTO, newNotification);
        NotificationUser notificationUser = saved.getNotificationUsers().get(0);
        return notificationUserMapper.toNotificationUserDTO(notificationUser);
    }

    @Override
    @Transactional
    public NotificationUserDTO updateNotificationIsReadStatus(String username, String notificationId, RequestNotificationUserDTO requestNotificationUserDTO) {
        NotificationUser notificationUser = notificationUserRepository.findByReceiverUsernameAndId(username, notificationId)
                .orElseThrow(() -> new NotFoundException("Notification", "id", notificationId));
        notificationUser.setIsRead(requestNotificationUserDTO.getIsRead());
        NotificationUser updated = notificationUserRepository.save(notificationUser);
        return notificationUserMapper.toNotificationUserDTO(updated);
    }

    @Transactional
    private Notification createNotification(RequestNotificationDTO requestNotificationDTO, Notification newNotification){
        if(requestNotificationDTO.getRepliedTo()!=null){
            Notification existingNotification = notificationRepository
                    .findById(requestNotificationDTO.getRepliedTo())
                    .orElseThrow(()-> new NotFoundException(
                            "Notification", requestNotificationDTO.getRepliedTo()));
            newNotification.setNotification(existingNotification);
        }

        NotificationUser newNotificationUser = notificationUserMapper
                .toNotificationUser(requestNotificationDTO, newNotification);

        if(requestNotificationDTO.getSenderType().equals(SenderType.USER)){
            User sender = userReaderService.getUserEntityByUsername(
                    requestNotificationDTO.getSenderUsername()
            );
            newNotificationUser.setSender(sender);
        }

        switch (requestNotificationDTO.getReceiverType()){
            case INDIVIDUAL -> {
                User receiver = userReaderService.getUserEntityByUsername(
                        requestNotificationDTO.getReceiverUsername()
                );
                newNotificationUser.setReceiver(receiver);
            }
            case GROUP -> {
                Role role = roleReaderService.getRoleEntityByName(
                        requestNotificationDTO.getReceiverRole()
                );
                newNotificationUser.setReceiverRole(role);
            }
        }

        newNotification.getNotificationUsers().add(newNotificationUser);

        return notificationRepository.save(newNotification);
    }
}
