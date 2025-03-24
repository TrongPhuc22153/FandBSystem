package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.ProductDiscount;


@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, ProductDiscount> {
    
}
