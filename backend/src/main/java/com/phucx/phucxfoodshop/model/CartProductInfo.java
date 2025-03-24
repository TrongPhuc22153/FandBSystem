package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class CartProductInfo extends OrderItem{
    private Boolean isSelected;

    public CartProductInfo(Integer productID, String productName, String categoryName, Integer quantity,
            Integer unitsInStock, String picture, BigDecimal unitPrice) {
        super(productID, productName, categoryName, quantity, unitsInStock, picture, unitPrice);
    }

    public CartProductInfo(Integer productID, String productName, String categoryName, Integer quantity,
            Integer unitsInStock, String picture, BigDecimal unitPrice, Boolean isSelected) {
        super(productID, productName, categoryName, quantity, unitsInStock, picture, unitPrice);
        this.isSelected = isSelected;
    }

    public CartProductInfo(Integer productID, String productName, String categoryName, Integer quantity,
            Integer unitsInStock, List<OrderItemDiscount> discounts, String picture, BigDecimal unitPrice,
            BigDecimal extendedPrice) {
        super(productID, productName, categoryName, quantity, unitsInStock, discounts, picture, unitPrice,
                extendedPrice);
    }

    public CartProductInfo(Integer productID, String productName, String categoryName, Integer quantity,
            Integer unitsInStock, List<OrderItemDiscount> discounts, String picture, BigDecimal unitPrice,
            BigDecimal extendedPrice, Boolean isSelected) {
        super(productID, productName, categoryName, quantity, unitsInStock, discounts, picture, unitPrice,
                extendedPrice);
        this.isSelected = isSelected;
    }

    public CartProductInfo() {
        super();
        this.isSelected = false;
    }
}
