package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.NotificationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, String>{
    @EntityGraph(attributePaths = {"order", "receiver", "notification"})
    List<NotificationUser> findByNotificationOrderOrderIdAndReceiverUsername(String orderId, String username);

    @EntityGraph(attributePaths = {"notification", "notification.topic"})
    Page<NotificationUser> findByReceiverUsername(String username, Pageable pageable);

    @EntityGraph(attributePaths = {"notification", "notification.topic"})
    Optional<NotificationUser> findByReceiverUsernameAndId(String username, String id);
}
