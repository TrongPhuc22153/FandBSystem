package com.phucx.phucxfoodshop.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.phucxfoodshop.model.Discount;



@Repository
public interface DiscountRepository extends JpaRepository<Discount, String>{
    @Modifying
    @Transactional
    @Query("""
        UPDATE Discount SET active=?2 WHERE discountID=?1
            """)
    public Integer updateDiscountStatus(String discountID, boolean status);

    @Query("""
        SELECT d \
        FROM ProductDiscount pd JOIN Discount d ON pd.discountID=d.discountID \
        WHERE d.discountID=?1 AND pd.productID=?2
        """)
    Optional<Discount> findByDiscountIDAndProductID(String discountID, Integer productID);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Discount SET discountPercent=?2, discountTypeID=?3, discountCode=?4, startDate=?5, endDate=?6 \
        WHERE discountID=?1 
            """)
    Integer updateDiscount(String discountID, Integer discountPercent, Integer discountTypeID,
        String discountCode, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        SELECT d \
        FROM Discount d JOIN ProductDiscount pd ON d.discountID=pd.discountID \
        WHERE pd.productID=?1
            """)
    Page<Discount> findByProductID(int productID, Pageable pageable);
}
