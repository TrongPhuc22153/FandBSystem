package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.ExistedProduct;

@Repository
public interface ExistedProductRepository extends JpaRepository<ExistedProduct, Integer>{
    
    
}
