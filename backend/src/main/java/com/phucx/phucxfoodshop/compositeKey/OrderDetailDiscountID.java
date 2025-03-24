package com.phucx.phucxfoodshop.compositeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDiscountID {
    private String orderID;
    private Integer productID;
    private String discountID;
}
