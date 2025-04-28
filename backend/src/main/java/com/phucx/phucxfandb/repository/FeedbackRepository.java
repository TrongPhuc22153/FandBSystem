package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findByIsDeletedFalse(Pageable pageable);
    Page<Feedback> findByOrderOrderIdAndIsDeletedFalse(String orderId, Pageable pageable);
    Page<Feedback> findByReservationReservationIdAndIsDeletedFalse(String reservationId, Pageable pageable);
    Page<Feedback> findByCustomerProfileUserUsernameAndIsDeletedFalse(String username, Pageable pageable);
    Optional<Feedback> findByIdAndCustomerProfileUserUsernameAndIsDeletedFalse(long id, String username);
    Optional<Feedback> findByIdAndIsDeletedFalse(long id);
}
