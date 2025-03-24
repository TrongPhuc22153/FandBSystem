package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.compositeKey.NotificationUserKey;
import com.phucx.phucxfoodshop.model.NotificationUser;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, NotificationUserKey>{
    @Procedure("UpdateNotificationReadStatusByNotificationIDAndUserID")
    Boolean updateNotificationReadStatusByNotificationIDAndUserID(String notificationID, String userID, Boolean isRead);
    
    @Procedure("UpdateNotificationReadStatusByUserID")
    Boolean updateNotificationReadStatusByUserID(String userID, Boolean isRead);

    @Procedure("UpdateNotificationReadStatusByNotificationID")
    Boolean updateNotificationReadStatusByNotificationID(String notificationID, Boolean isRead);

    @Procedure("UpdateNotificationsReadByNotificationID")
    Boolean updateNotificationsReadByNotificationID(String notificationIDs, Boolean isRead);
}
