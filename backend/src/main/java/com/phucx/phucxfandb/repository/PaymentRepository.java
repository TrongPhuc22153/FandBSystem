package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>{

//    @Transactional(readOnly = true)
//    @EntityGraph(attributePaths = {"order", "customer"})
//    Optional<Payment> findByOrderOrderId(String orderId);
//
//    @Transactional(readOnly = true)
//    @EntityGraph(attributePaths = {"reservation", "customer"})
//    Optional<Payment> findByReservationReservationId(String reservationId);
}
