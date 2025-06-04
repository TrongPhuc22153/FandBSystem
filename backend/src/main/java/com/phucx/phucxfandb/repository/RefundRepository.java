package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {
    boolean existsByPaymentPaymentId(String paymentId);
}
