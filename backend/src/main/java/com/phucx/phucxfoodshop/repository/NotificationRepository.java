package com.phucx.phucxfoodshop.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>{
    @Procedure("CreateNotification")
    Boolean createNotification(String notificationID, String title, 
        String message, String picture, String senderID, String receiverID, 
        String topicName, String repliedTo, String status, Boolean isRead, 
        LocalDateTime time);
}
