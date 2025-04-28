package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    Page<Reservation> findByCustomerProfileUserUsernameAndStatus(String username, ReservationStatus status, Pageable pageable);

    Optional<Reservation> findByCustomerProfileUserUsernameAndReservationId(String username, String reservationId);

    Optional<Reservation> findByReservationIdAndCustomerCustomerId(String reservationId, String customerId);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
}
