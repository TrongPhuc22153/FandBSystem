package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;



@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByTableTableId(String tableId);

    Optional<Order> findByOrderIdAndType(String orderId, OrderType type);

    Page<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);


    Optional<Order> findByOrderIdAndCustomerProfileUserUsername(String orderId, String username);

    Optional<Order> findByOrderIdAndEmployeeProfileUserUsername(String orderId, String username);

    Optional<Order> findByStatusAndOrderId(OrderStatus status, String orderId);

    Page<Order> findByCustomerProfileUserUsernameAndStatus(String username, OrderStatus status, Pageable pageable);

    Page<Order> findByCustomerProfileUserUsername(String username, Pageable pageable);


    Page<Order> findByEmployeeEmployeeIdAndStatus(String employeeId, OrderStatus status, Pageable pageable);


    Page<Order> findByEmployeeProfileUserUsername(String username, Pageable pageable);

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
    Page<Order> findAll(Specification<Order> spec, Pageable pageable);
} 
