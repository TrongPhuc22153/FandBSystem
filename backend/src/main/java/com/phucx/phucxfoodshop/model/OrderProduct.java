package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    private Integer productID;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private Integer discount;
    private BigDecimal extendedPrice;
    private String picture;
}
