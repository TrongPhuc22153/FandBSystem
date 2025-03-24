package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class OrderItem {
    private Integer productID;
    private String productName;
    private String categoryName;
    private Integer quantity;
    private Integer unitsInStock;
    private List<OrderItemDiscount> discounts;
    private String picture;
    private Integer totalDiscount;
    private BigDecimal unitPrice;

    private BigDecimal extendedPrice;



    public OrderItem(Integer productID, String productName, String categoryName, Integer quantity, Integer unitsInStock, String picture, BigDecimal unitPrice) {
        this();
        this.productName=productName;
        this.productID = productID;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.unitsInStock = unitsInStock;
        this.picture = picture;
        this.unitPrice = unitPrice;
    }



    public OrderItem(Integer productID, String productName, String categoryName, Integer quantity, Integer unitsInStock, List<OrderItemDiscount> discounts, String picture,
            BigDecimal unitPrice, BigDecimal extendedPrice) {
        this.productName = productName;
        this.productID = productID;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.unitsInStock = unitsInStock;
        this.discounts = discounts;
        this.picture = picture;
        this.unitPrice = unitPrice;
        this.extendedPrice = extendedPrice;
    }

    public OrderItem(Integer productID, String productName, Integer quantity, String picture, BigDecimal unitPrice) {
        this.productName=productName;
        this.productID = productID;
        this.quantity = quantity;
        this.picture = picture;
        this.unitPrice = unitPrice;
    }

    public OrderItem() {
        this.discounts = new ArrayList<>();
        this.extendedPrice = BigDecimal.valueOf(0);
    }
}
