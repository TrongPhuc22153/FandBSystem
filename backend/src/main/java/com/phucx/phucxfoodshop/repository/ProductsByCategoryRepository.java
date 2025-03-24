package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.compositeKey.ProductsByCategoryID;
import com.phucx.phucxfoodshop.model.ProductsByCategory;

@Repository
public interface ProductsByCategoryRepository extends JpaRepository<ProductsByCategory, ProductsByCategoryID>{
    
}
