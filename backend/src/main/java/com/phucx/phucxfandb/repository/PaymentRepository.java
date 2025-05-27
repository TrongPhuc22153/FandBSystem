package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByPaypalOrderId(String paypalOrderId);

}
