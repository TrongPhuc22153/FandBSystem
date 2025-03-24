package com.phucx.phucxfoodshop.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.compositeKey.OrderDetailKey;
import com.phucx.phucxfoodshop.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey>{
    @Query("""
        SELECT od FROM OrderDetail od \
        WHERE od.orderID=?1
        """)
    List<OrderDetail> findByOrderID(String orderID);

    @Procedure("InsertOrderDetail")
    Boolean insertOrderDetail(Integer productID, String orderID, BigDecimal unitPrice, Integer quantity);
    @Procedure("CreateOrderDetail")
    Boolean createOrderDetail(Integer productID, String orderID, BigDecimal unitPrice, Integer quantity);
}
