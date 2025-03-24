package com.phucx.phucxfoodshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.ProductSize;


@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, String>{
    @Procedure("CreateProductSize")
    public Boolean createProductSize(String productsizeid, 
        Integer productid, Integer height, Integer width, 
        Integer length, Integer weight);

    @Procedure("UpdateProductSize")
    public Boolean updateProductSize(Integer productID, 
        Integer height, Integer width, Integer length, 
        Integer weight);

    public Optional<ProductSize> findByProductID(Integer productID);
}
