package com.phucx.phucxfoodshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import com.phucx.phucxfoodshop.compositeKey.OrderDetailDiscountID;
import com.phucx.phucxfoodshop.model.OrderDetailDiscount;

@Repository
public interface OrderDetailDiscountRepository extends JpaRepository<OrderDetailDiscount, OrderDetailDiscountID> {
    @Procedure("InsertOrderDetailDiscount")
    Boolean insertOrderDetailDiscount(String orderID, Integer productID, String discountID, Integer discountPercent, LocalDateTime appliedDate);

    @Procedure("CreateOrderDetailDiscount")
    Boolean createOrderDetailDiscount(String orderID, Integer productID, String discountID, Integer discountPercent, LocalDateTime appliedDate);

    @Query("""
        SELECT odd \
        FROM  OrderDetailDiscount odd \
        WHERE odd.orderID=?1 AND odd.productID=?2 
        """)
    List<OrderDetailDiscount> findByOrderIDAndProductID(String orderID, Integer productID);
}
