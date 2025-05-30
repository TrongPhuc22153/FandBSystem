package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Payment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByPaypalOrderId(String paypalOrderId);

    @NotNull
    @EntityGraph(attributePaths = {"order", "reservation"})
    Page<Payment> findAll(Specification<Payment> spec, @NotNull Pageable pageable);
}
