package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.enums.PaymentStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByPaypalOrderId(String paypalOrderId);

    @NotNull
    @EntityGraph(attributePaths = {"order", "reservation"})
    Page<Payment> findAll(Specification<Payment> spec, @NotNull Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0) FROM Payment p
            WHERE p.paymentDate >= :startOfDay AND p.paymentDate < :endOfDay
                AND p.status = :status
            """)
    Long sumTotalPaymentByStatus(
            @Param("status")PaymentStatus status,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
            SELECT CAST(p.paymentDate AS date), SUM(p.amount)
            FROM Payment p
            WHERE p.status = :status
              AND p.paymentDate >= :startOfWeek
              AND p.paymentDate < :endOfWeek
            GROUP BY CAST(p.paymentDate AS date)
        """)
    List<Object[]> getDailyPaymentsThisWeek(
            @Param("startOfWeek") LocalDateTime startOfWeek,
            @Param("endOfWeek") LocalDateTime endOfWeek,
            @Param("status") PaymentStatus status);

    boolean existsByPaypalOrderIdAndStatus(String paypalOrderId, PaymentStatus status);
}
