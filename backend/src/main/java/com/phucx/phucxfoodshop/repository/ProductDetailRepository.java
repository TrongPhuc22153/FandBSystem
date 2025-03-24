package com.phucx.phucxfoodshop.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer>{
    @Procedure(name = "UpdateProduct")
    Boolean updateProduct(
        @Param("productId") Integer productID, 
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") BigDecimal unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("description") String description, 
        @Param("categoryID")Integer categoryID
    );

    @Procedure(name = "InsertProduct")
    Boolean insertProduct(
        @Param("productName") String productName, 
        @Param("quantityPerUnit") String quantityPerUnit, 
        @Param("unitPrice") BigDecimal unitPrice, 
        @Param("unitsInStock") Integer unitsInStock, 
        @Param("discontinued") Boolean discontinued,
        @Param("picture") String picture, 
        @Param("description") String description, 
        @Param("categoryID")Integer categoryID
    );

    Optional<ProductDetail> findByProductName(String productName);
}
