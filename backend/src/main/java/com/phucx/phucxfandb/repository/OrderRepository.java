package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByOrderIdAndType(String orderId, OrderType type);

    Optional<Order> findByOrderIdAndCustomerProfileUserUsername(String orderId, String username);

    Optional<Order> findByOrderIdAndEmployeeProfileUserUsername(String orderId, String username);

    @EntityGraph(attributePaths = {
            "orderDetails",
            "customer",
            "customer.profile",
            "customer.profile.user",
            "employee",
            "employee.profile",
            "employee.profile.user",
    })
    @NonNull
    Page<Order> findAll(@Nullable Specification<Order> spec, @NonNull Pageable pageable);

    @Query("""
            SELECT COUNT(o) FROM Order o
            WHERE o.orderDate BETWEEN :startOfDay AND :endOfDay
            """)
    Long countTotalOrders(@Param("startOfDay") LocalDateTime startOfDay,
                          @Param("endOfDay") LocalDateTime endOfDay);

    @Query("""
            SELECT COUNT(o) FROM Order o
            WHERE o.orderDate BETWEEN :startOfDay AND :endOfDay
                AND o.status = :status
            """)
    Long countTotalOrdersByStatus(
            @Param("status") OrderStatus status,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
          SELECT COALESCE(AVG(o.totalPrice), 0)
          FROM Order o
          WHERE o.payment.status = :paymentStatus
            AND o.orderDate BETWEEN :startDate AND :endDate
          """)
    BigDecimal calculateAverageOrderValueWithPaymentStatus(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("paymentStatus")PaymentStatus paymentStatus);



} 
