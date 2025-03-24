package com.phucx.phucxfoodshop.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Immutable
@Entity @Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "current product list")
public class CurrentProduct implements Serializable{
    @Id
    private Integer productID;
    private String productName;
    private BigDecimal unitPrice;
    private Integer unitsInStock;
    private String picture;

    private String discountID;
    private Integer discountPercent;

    private String categoryName;
}
