package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.entity.Order;
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
            "table"
    })
    @NonNull
    Page<Order> findAll(@Nullable Specification<Order> spec, @NonNull Pageable pageable);
} 
