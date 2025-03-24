package com.phucx.phucxfoodshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.compositeKey.OrderDetailKey;
import com.phucx.phucxfoodshop.constant.OrderStatus;
import com.phucx.phucxfoodshop.model.OrderDetailExtended;

import java.util.List;


@Repository
public interface OrderDetailExtendedRepository extends JpaRepository<OrderDetailExtended, OrderDetailKey>{

    List<OrderDetailExtended> findByOrderID(String orderID);
    List<OrderDetailExtended> findByOrderIDAndStatus(String orderID, OrderStatus status);   

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.customerID=?1 \
        ORDER BY o.orderDate ASC
            """)
    Page<OrderDetailExtended> findAllByCustomerIDOrderByOrderDateAsc(String customerID, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.employeeID=?1 \
        ORDER BY o.orderDate Asc
            """)
    Page<OrderDetailExtended> findAllByEmployeeIDOrderByOrderDateAsc(String employeeID, Pageable pageable);


    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.customerID=?1 \
        ORDER BY o.orderDate DESC
            """)
    Page<OrderDetailExtended> findAllByCustomerIDOrderByOrderDateDesc(String customerID, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.employeeID=?1 \
        ORDER BY o.orderDate DESC
            """)
    Page<OrderDetailExtended> findAllByEmployeeIDOrderByOrderDateDesc(String employeeID, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.customerID=?1 AND ode.status=?2 \
        ORDER BY o.orderDate DESC
            """)
    Page<OrderDetailExtended> findAllByCustomerIDAndStatusOrderByOrderDateDesc(
        String customerID, OrderStatus status, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.employeeID=?1 AND ode.status=?2 \
        ORDER BY o.orderDate DESC
            """)
    Page<OrderDetailExtended> findAllByEmployeeIDAndStatusOrderByOrderDateDesc(
        String employeeID, OrderStatus status, Pageable pageable);

    @Query("""
        SELECT ode \
        FROM Order o JOIN OrderDetailExtended ode ON o.orderID=ode.orderID \
        WHERE ode.status=?1 \
        ORDER BY o.orderDate DESC
            """)
    Page<OrderDetailExtended> findAllByStatusOrderByOrderDateDesc(OrderStatus status, Pageable pageable);
}
