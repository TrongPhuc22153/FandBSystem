package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>{

    boolean existsByOrderOrderIdAndTitle(String orderId, String title);

    boolean existsByReservationReservationIdAndTitle(String reservationId, String title);

}
