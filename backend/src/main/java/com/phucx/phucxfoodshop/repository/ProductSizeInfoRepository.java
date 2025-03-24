package com.phucx.phucxfoodshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.ProductSizeInfo;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProductSizeInfoRepository extends JpaRepository<ProductSizeInfo, Integer>{
    @Query("""
        SELECT p FROM ProductSizeInfo p WHERE p.productName IN ?1
            """)
    List<ProductSizeInfo> findAllByProductNames(List<String> productNames);

    @Procedure("CreateProduct")
    Boolean createProduct(
        String productName, 
        String quantityPerUnit, 
        BigDecimal unitPrice,
        Integer unitsInStock,
        Boolean discontinued,
        String picture,
        String description,
        Integer categoryID,
        Integer height,
        Integer width,
        Integer length,
        Integer weight);
}
