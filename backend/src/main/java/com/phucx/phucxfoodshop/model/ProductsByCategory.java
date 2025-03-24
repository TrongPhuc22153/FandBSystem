package com.phucx.phucxfoodshop.model;

import com.phucx.phucxfoodshop.compositeKey.ProductsByCategoryID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity 
@Data @ToString
@Table(name = "products by category")
@IdClass(ProductsByCategoryID.class)
public class ProductsByCategory {
    @Id
    private String categoryName;
    @Id
    private String productName;
    private String quantityPerUnit;
    private Integer unitsInStock;
    private Boolean discontinued;
}
