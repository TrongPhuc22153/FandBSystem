package com.phucx.phucxfoodshop.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable @Data
public class OrderDetailsKey {
    private Integer productID;
    private Integer orderID;

}
