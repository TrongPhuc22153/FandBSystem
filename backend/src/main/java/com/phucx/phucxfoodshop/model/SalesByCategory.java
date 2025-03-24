package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class SalesByCategory{
    private Integer productID;
    private Integer categoryID;
    private String categoryName;
    private String productName;
    private BigDecimal productSales;
}
