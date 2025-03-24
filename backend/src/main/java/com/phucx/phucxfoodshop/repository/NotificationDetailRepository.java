package com.phucx.phucxfoodshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.NotificationDetail;

@Repository
public interface NotificationDetailRepository extends JpaRepository<NotificationDetail, String>{
    @Query("""
        SELECT n FROM NotificationDetail n \
        WHERE n.notificationID=?1 AND receiverID=?2 \
        ORDER BY n.time DESC
            """)
    Optional<NotificationDetail> findByNotificationIDAndReceiverIDOrderByTimeDesc(String notificationID, String receiverID);

    @Query("""
        SELECT n FROM NotificationDetail n \
        WHERE n.notificationID=?1 AND (receiverID=?2 OR receiverID=?3) \
        ORDER BY n.time DESC 
            """)
    Optional<NotificationDetail> findByNotificationIDAndReceiverIDOrBroadCastOrderByTimeDesc(String notificationID, String receiverID, String broadCast);

    @Query("""
        SELECT n FROM NotificationDetail n \
        WHERE n.topic=?1 AND (receiverID=?2 OR receiverID=?3)
            """)
    Page<NotificationDetail> findByTopicAndReceiverIDOrBroadCast(String topicName, String receiverID, String broadCast, Pageable pageable);

    @Query("""
        SELECT n FROM NotificationDetail n WHERE n.topic=?1
            """)
    Page<NotificationDetail> findByTopicName(String topicName, Pageable pageable);

    @Query("""
        SELECT n FROM NotificationDetail n WHERE n.topic=?1
            """)
    List<NotificationDetail> findByTopicName(String topicName);

    Optional<NotificationDetail> findByNotificationIDAndReceiverID(String notificationID, String receiverID);

    @Query("""
        SELECT n FROM NotificationDetail n \
        WHERE (receiverID=?1 OR receiverID=?2) \
        ORDER BY time DESC
            """)
    Page<NotificationDetail> findByReceiverIDOrBroadCastOrderByTimeDesc(String receiverID, String broadCast, Pageable pageable);

    Page<NotificationDetail> findByReceiverIDOrderByTimeDesc(String receiverID, Pageable pageable);

    @Query("""
        SELECT COUNT(n) FROM NotificationDetail n \
        WHERE (receiverID=?1 OR receiverID=?3) AND isRead=?2
            """)
    Long countNumberOfNotificationsByReceiverIDOrBroadCastAndIsRead(String receiverID, Boolean isRead, String broadCast);

    @Query("""
        SELECT n FROM NotificationDetail n \
        WHERE title=?1 AND receiverID=?2 AND message LIKE ?3
            """)
    List<NotificationDetail> findByTitleAndReceiverIDAndMessageLike(String title, String receiverID, String message);
}
