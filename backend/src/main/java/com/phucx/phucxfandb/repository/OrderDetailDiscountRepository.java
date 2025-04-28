package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.OrderDetailDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailDiscountRepository extends JpaRepository<OrderDetailDiscount, String> {

}
