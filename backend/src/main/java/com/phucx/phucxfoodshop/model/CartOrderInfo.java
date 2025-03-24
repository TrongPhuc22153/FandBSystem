package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class CartOrderInfo {
    private List<CartProductInfo> products;
    private BigDecimal freight;
    private BigDecimal totalPrice;


    
    public CartOrderInfo(List<CartProductInfo> products, BigDecimal freight, BigDecimal totalPrice) {
        this.products = products;
        this.freight = freight;
        this.totalPrice = totalPrice;
    }
    public CartOrderInfo(BigDecimal freight, BigDecimal totalPrice) {
        this();
        this.freight = freight;
        this.totalPrice = totalPrice;
    }
    public CartOrderInfo() {
        this.products = new ArrayList<>();
        this.freight = BigDecimal.valueOf(0);
        this.totalPrice = BigDecimal.valueOf(0);
    }
}
