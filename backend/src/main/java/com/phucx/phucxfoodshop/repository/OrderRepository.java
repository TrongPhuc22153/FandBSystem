package com.phucx.phucxfoodshop.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.model.Order;



@Repository
public interface OrderRepository extends JpaRepository<Order, String>{

    Optional<Order> findByOrderIDAndStatus(String orderID, OrderStatus status);
    Optional<Order> findByOrderIDAndEmployeeIDAndStatus(String orderID, String employeeID, OrderStatus status);

    @Procedure("InsertOrder")
    public Boolean insertOrder(String orderID, 
        LocalDateTime orderDate, LocalDateTime requiredDate, LocalDateTime shippedDate, 
        BigDecimal freight, String shipName, String shipAddress, String shipCity, 
        String phone, String status, String customerID, String employeeID, Integer shipperID);

    @Procedure("CreateOrder")
    public Boolean createOrder(String orderID, 
        LocalDateTime orderDate, LocalDateTime 
        requiredDate, LocalDateTime shippedDate, 
        BigDecimal freight, String shipName, 
        String shipAddress, String shipCity, 
        String shipDistrict, String shipWard,
        String phone, String status, String customerID, 
        String employeeID, Integer shipperID);


    @Procedure("UpdateOrderStatus")
    public Boolean updateOrderStatus(String orderID, String status);

    @Procedure("UpdateOrderEmployeeID")
    public Boolean updateOrderEmployeeID(String orderID, String employeeID);
    
    @Query("""
        SELECT o FROM Order o WHERE o.status=?1    
        """)
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("""
        SELECT o FROM Order o WHERE o.status=?1 AND o.orderID=?2   
        """)
    Optional<Order> findByStatusAndOrderID(OrderStatus status, String orderID);

    @Query("""
        SELECT o FROM Order o WHERE o.status=?1 AND o.employeeID=?2
        """)
    Page<Order> findByStatusAndEmployeeID(OrderStatus status, String employeeID, Pageable pageable);

    @Query("""
        SELECT o FROM Order o WHERE o.customerID=?1
        """)
    Page<Order> findByCustomerID(String customerID, Pageable page);    

    @Query("""
        SELECT o FROM Order o WHERE o.employeeID=?1 AND o.orderID=?2
            """)
    Optional<Order> findByEmployeeIDAndOrderID(String employeeID, String orderID);

    @Query("""
        SELECT COUNT(o) FROM Order o WHERE o.status=?1
            """)
    Optional<Long> countOrderByStatus(OrderStatus status);
} 
