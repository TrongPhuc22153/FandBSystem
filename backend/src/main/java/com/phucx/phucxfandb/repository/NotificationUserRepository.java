package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.NotificationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, String>, JpaSpecificationExecutor<NotificationUser> {
    @EntityGraph(attributePaths = {"notification", "notification.topic"})
    Page<NotificationUser> findByReceiverUsername(String username, Pageable pageable);

    @EntityGraph(attributePaths = {"notification", "notification.topic"})
    Optional<NotificationUser> findByReceiverUsernameAndId(String username, String id);

    @NonNull
    @EntityGraph(attributePaths = {"notification", "notification.topic"})
    Page<NotificationUser> findAll(@Nullable Specification<NotificationUser> spec, @NonNull Pageable pageable);
}
