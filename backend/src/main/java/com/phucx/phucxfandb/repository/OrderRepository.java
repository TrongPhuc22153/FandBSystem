package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import com.phucx.phucxfandb.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Repository
public interface OrderRepository extends JpaRepository<Order, String>{

    Optional<Order> findByTableTableId(String tableId);

    Optional<Order> findByOrderIdAndType(String orderId, OrderType type);

    Page<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByStatusIn(List<OrderStatus> statuses, Pageable pageable);

    Optional<Order> findByOrderIdAndCustomerProfileUserUsername(String orderId, String username);

    Optional<Order> findByOrderIdAndEmployeeProfileUserUsername(String orderId, String username);

    Optional<Order> findByStatusAndOrderId(OrderStatus status, String orderId);

    Page<Order> findByCustomerCustomerId(String customerId, Pageable pageable);

    Page<Order> findByCustomerCustomerIdAndStatus(String customerId, OrderStatus status, Pageable pageable);

    Page<Order> findByCustomerProfileUserUsernameAndStatus(String username, OrderStatus status, Pageable pageable);

    Page<Order> findByCustomerProfileUserUsername(String username, Pageable pageable);

    Page<Order> findByEmployeeEmployeeId(String employeeId, Pageable pageable);

    Page<Order> findByEmployeeEmployeeIdAndStatus(String employeeId, OrderStatus status, Pageable pageable);

    Page<Order> findByEmployeeProfileUserUsernameAndStatus(String username, OrderStatus status, Pageable pageable);

    Page<Order> findByEmployeeProfileUserUsername(String username, Pageable pageable);

    Optional<Long> countOrderByStatus(OrderStatus status);
} 
