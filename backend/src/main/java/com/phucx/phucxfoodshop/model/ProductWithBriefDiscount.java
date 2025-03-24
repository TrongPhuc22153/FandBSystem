package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class ProductWithBriefDiscount {
    private Integer productID;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String picture;
    private List<DiscountBreifInfo> discounts;
    private Integer totalDiscount;
    private BigDecimal extendedPrice;
    
    public ProductWithBriefDiscount(Integer productID, String productName, BigDecimal unitPrice, Integer quantity,
            String picture, List<DiscountBreifInfo> discounts, Integer totalDiscount, BigDecimal extendedPrice) {
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.picture = picture;
        this.discounts = discounts;
        this.totalDiscount = totalDiscount;
        this.extendedPrice = extendedPrice;
    }

    public ProductWithBriefDiscount(Integer productID, String productName, BigDecimal unitPrice, Integer quantity,
            String picture, BigDecimal extendedPrice) {
        this();
        this.productID = productID;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.picture = picture;
        this.extendedPrice = extendedPrice;
    }

    public ProductWithBriefDiscount() {
        this.discounts = new ArrayList<>();
        this.totalDiscount = Integer.valueOf(0);
    }
}
