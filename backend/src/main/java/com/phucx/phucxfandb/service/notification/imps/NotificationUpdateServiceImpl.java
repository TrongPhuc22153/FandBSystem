package com.phucx.phucxfandb.service.notification.imps;

import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
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
import com.phucx.phucxfandb.service.topic.TopicReaderService;
import com.phucx.phucxfandb.service.user.UserReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
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

    @Override
    @Modifying
    @Transactional
    public NotificationUserDTO createOrderNotification(String username, String orderId, RequestNotificationDTO requestNotificationDTO) {
        log.info("createOrderNotification(username={}, orderId={}, requestNotificationDTO={})",
                username, orderId, requestNotificationDTO);
        Topic topic = topicReaderService.getTopicEntity(requestNotificationDTO.getTopic());
        Order order = orderReaderService.getOrderEntity(orderId);

        Notification newNotification = notificationMapper
                .toOrderNotification(requestNotificationDTO, order, topic);

        if(requestNotificationDTO.getRepliedTo()!=null){
            Notification existingNotification = notificationRepository.findById(requestNotificationDTO.getRepliedTo())
                    .orElseThrow(()-> new NotFoundException("Notification", requestNotificationDTO.getRepliedTo()));
            newNotification.setNotification(existingNotification);
        }

        Notification savedNotification = notificationRepository.save(newNotification);
        User sender = userReaderService.getUserEntityByUsername(username);
        User receiver = userReaderService.getUserEntityByUsername(requestNotificationDTO.getReceiverUsername());

        NotificationUser newNotificationUser = notificationUserMapper.toNotification(
                requestNotificationDTO,
                sender,
                receiver,
                savedNotification
        );

        NotificationUser notificationUser = notificationUserRepository.save(newNotificationUser);
        return notificationUserMapper.toNotificationUserDTO(notificationUser);
    }

    @Override
    @Modifying
    @Transactional
    public NotificationUserDTO createReservationNotification(String username, String reservationId, RequestNotificationDTO requestNotificationDTO) {
        log.info("createReservationNotification(username={}, reservationId={}, requestNotificationDTO={})",
                username, reservationId, requestNotificationDTO);
        Topic topic = topicReaderService.getTopicEntity(requestNotificationDTO.getTopic());
        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);

        Notification newNotification = notificationMapper
                .toReservationNotification(requestNotificationDTO, reservation, topic);

        if(requestNotificationDTO.getRepliedTo()!=null){
            Notification existingNotification = notificationRepository.findById(requestNotificationDTO.getRepliedTo())
                    .orElseThrow(()-> new NotFoundException("Notification", requestNotificationDTO.getRepliedTo()));
            newNotification.setNotification(existingNotification);
        }

        Notification savedNotification = notificationRepository.save(newNotification);
        User sender = userReaderService.getUserEntityByUsername(username);
        User receiver = userReaderService.getUserEntityByUsername(requestNotificationDTO.getReceiverUsername());

        NotificationUser newNotificationUser = notificationUserMapper.toNotification(
                requestNotificationDTO,
                sender,
                receiver,
                savedNotification
        );

        NotificationUser notificationUser = notificationUserRepository.save(newNotificationUser);
        return notificationUserMapper.toNotificationUserDTO(notificationUser);
    }
}
