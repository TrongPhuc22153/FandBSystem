package com.phucx.phucxfoodshop.service.notification.imps;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.NotificationIsRead;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.NotificationDetail;
import com.phucx.phucxfoodshop.model.OrderNotificationDTO;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.repository.NotificationDetailRepository;
import com.phucx.phucxfoodshop.repository.NotificationRepository;
import com.phucx.phucxfoodshop.repository.NotificationUserRepository;
import com.phucx.phucxfoodshop.service.notification.NotificationService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService{
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationDetailRepository notificationDetailRepository;
    @Autowired
    private NotificationUserRepository notificationUserRepository;
    @Autowired
    private UserService userService;

    @Override
    public NotificationDetail createNotification(NotificationDetail notification) {
        log.info("createNotification({})", notification.toString());
        String notificationID = UUID.randomUUID().toString();
        // create new notification
        Boolean status = notificationRepository.createNotification(
            notificationID, notification.getTitle(), notification.getMessage(), 
            notification.getPicture(), notification.getSenderID(), 
            notification.getReceiverID(), notification.getTopic(), 
            notification.getRepliedTo(), notification.getStatus(), 
            NotificationIsRead.NO.getValue(), notification.getTime());

        notification.setNotificationID(notificationID);
        if(!status) throw new RuntimeException("Error during saving the notification: " + notification.toString());

        return notification;
    }

    @Override
    public NotificationDetail getNotificationsByID(String notificationID) throws NotFoundException {
        return notificationDetailRepository.findById(notificationID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " does not found"));
    }

    @Override
    public Page<NotificationDetail> getNotificationsByTopicName(String topicName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByTopicName(topicName, pageable);
    }

    @Override
    public List<NotificationDetail> getNotificationsByTopicName(String topicName) {
        return notificationDetailRepository.findByTopicName(topicName);
    }

    @Override
    public Page<NotificationDetail> getNotificationsByReceiverID(String userID, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByReceiverIDOrderByTimeDesc(userID, pageable);
    }

    @Override
    public Boolean updateNotificationReadStatusOfUser(String notificationID, String userID, Boolean isRead) 
    throws NotFoundException {
        log.info("updateNotificationReadStatus(notificationID={}, userID={}, isRead={})", notificationID, userID, isRead);
        return updateNotificationReadStatus(notificationID, userID, isRead);
    }

    @Override
    public Page<NotificationDetail> getNotificationsByReceiverIDAndTopicName(
        String userID, NotificationBroadCast broadCast, String topicName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByTopicAndReceiverIDOrBroadCast(
            topicName, userID, broadCast.name(), pageable);
    }

    @Override
    public Page<NotificationDetail> getNotificationsByReceiverIDOrBroadCast(
        String userID, NotificationBroadCast broadCast, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return notificationDetailRepository.findByReceiverIDOrBroadCastOrderByTimeDesc(
            userID, broadCast.name(), pageable);
    }

    @Override
    public NotificationDetail getNotificationByUserIDAndNotificationID(String userID, String notificationID) throws NotFoundException {
        NotificationDetail notification = notificationDetailRepository.findByNotificationIDAndReceiverIDOrderByTimeDesc(notificationID, userID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public NotificationDetail getNotificationByUserIDOrBroadCastAndNotificationID(String userID, NotificationBroadCast broadCast, String notificationID) 
    throws NotFoundException {
        NotificationDetail notification = notificationDetailRepository
        .findByNotificationIDAndReceiverIDOrBroadCastOrderByTimeDesc(notificationID, userID, broadCast.name())
        .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of User " + userID + " does not found"));
        return notification;
    }

    @Override
    public Long getUserTotalNumberOfUnreadNotifications(String userID, NotificationBroadCast broadCast) {
        Long numberOfUnreadNotifications = notificationDetailRepository
            .countNumberOfNotificationsByReceiverIDOrBroadCastAndIsRead(
                userID, NotificationIsRead.NO.getValue(), broadCast.name());
        return numberOfUnreadNotifications;
    }

    @Override
    public Boolean updateNotificationsReadStatus(String userID, Boolean status){
        log.info("updateNotificationReadStatus(userID={}, status={})", userID, status);
        return this.notificationUserRepository.updateNotificationReadStatusByUserID(userID, status);
    }

    @Override
    public Boolean updateNotificationReadStatusOfBroadcast(String notificationID, NotificationBroadCast broadCast, Boolean status)
            throws NotFoundException {
        log.info("updateNotificationReadStatusOfBroadcast(notificationID={}, broadCast={}, status={})", notificationID, broadCast, status);
        return updateNotificationReadStatus(notificationID, broadCast.name(), status);
    }

    private Boolean updateNotificationReadStatus(String notificationID, String receiverID, Boolean isRead) throws NotFoundException{
        log.info("updateNotificationReadStatus(notificationID={}, receiverID={}, isRead={})", notificationID, receiverID, isRead);
        // fetch notifiation from db
        NotificationDetail fetchedNotificationDetail = this.notificationDetailRepository
            .findByNotificationIDAndReceiverID(notificationID, receiverID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of " + receiverID + " does not found"));
        // update read statu for receiverID
        return notificationUserRepository.updateNotificationReadStatusByNotificationIDAndUserID(
            fetchedNotificationDetail.getNotificationID(), receiverID, isRead);
    }

    @Override
    public NotificationDetail getNotificationByNotificationIDAndReceiverID(String notificationID, String receiverID)
            throws NotFoundException {
        return this.notificationDetailRepository
            .findByNotificationIDAndReceiverID(notificationID, receiverID)
            .orElseThrow(()-> new NotFoundException("Notification " + notificationID + " of " + receiverID + " does not found"));
    }

    @Override
    public Boolean updateNotificationReadStatus(String notificationID, Boolean status) {
        log.info("updateNotificationReadStatus(notificationID={}, status={})", notificationID, status);
        return this.notificationUserRepository.updateNotificationReadStatusByNotificationID(notificationID, status);
    }

    @Override
    public Boolean updateNotificationReadStatusOfBroadcast(String title, String message,
            NotificationBroadCast broadCast, Boolean status) throws NotFoundException {
        log.info("updateNotificationReadStatusOfBroadcast(title={}, message={}, broadCast={}, status={})", 
            title, message, broadCast, status);
        // fetch notifications
        List<NotificationDetail> fetchedNotifications = notificationDetailRepository
            .findByTitleAndReceiverIDAndMessageLike(title, broadCast.name(), "%" + message + "%");
        // check notifications
        if(fetchedNotifications==null || fetchedNotifications.isEmpty()) {
            log.info("message={}", fetchedNotifications);
            throw new NotFoundException("Notification with title " + title + " and message " + 
                message + " of " + broadCast.name() + " does not found");
        }
        // extract notificationID
        List<String> notificationIDs = fetchedNotifications.stream()
            .map(NotificationDetail::getNotificationID)
            .collect(Collectors.toList());
        String notificationIDsStr = String.join(",", notificationIDs);
        // update notification isRead status
        Boolean result = notificationUserRepository.updateNotificationsReadByNotificationID(notificationIDsStr, status);
        return result;
    }

    @Override
    public NotificationDetail getOrderNotificationDetail(String title, String orderID, String receiverID) throws NotFoundException {
        log.info("getOrderNotificationDetail(title={}, orderID={}, receiverID={})", title, orderID, receiverID);
        List<NotificationDetail> fetchedNotifications = notificationDetailRepository
            .findByTitleAndReceiverIDAndMessageLike(title, receiverID, "%" + orderID + "%");
        if(fetchedNotifications==null || fetchedNotifications.isEmpty())
            throw new NotFoundException("Order notification with orderID " + orderID + " and title " + title +
                " of receiver " + receiverID + " does not found!");
        return fetchedNotifications.get(0);
        
    }

    @Override
    public void markAsReadForConfirmedOrderNotification(OrderNotificationDTO notification) throws NotFoundException{
        log.info("markAsReadForConfirmedOrderNotification({})", notification);
        // mark pending order notification as read after confirming
        Boolean updatedStatus = this.updateNotificationReadStatusOfBroadcast(
            notification.getTitle().name(), notification.getOrderID(), 
            NotificationBroadCast.ALL_EMPLOYEES, NotificationIsRead.YES.getValue());
        if(!updatedStatus) throw new RuntimeException("Error while updating notification "+ 
            notification.toString() + " read status");  
    }

    @Override
    public Page<NotificationDetail> getNotificationsByUsernameOrBroadCast(String username,
            NotificationBroadCast broadCast, int pageNumber, int pageSize) {
        log.info("getNotificationsByUsernameOrBroadCast(username={}, broadcast={}, pagenumber={}, pagesize={})",
            username, broadCast, pageNumber, pageSize);
        User user = userService.getUser(username);
        return this.getNotificationsByReceiverIDOrBroadCast(
            user.getUserID(), broadCast, pageNumber, pageSize);
        
    }

    @Override
    public Long getUserTotalNumberOfUnreadNotificationsByUsername(String username, NotificationBroadCast broadCast) {
        log.info("getUserTotalNumberOfUnreadNotificationsByUsername(username, broadcast={})", username, broadCast);
        User user = userService.getUser(username);
        return this.getUserTotalNumberOfUnreadNotifications(user.getUserID(), broadCast);
    }
}
