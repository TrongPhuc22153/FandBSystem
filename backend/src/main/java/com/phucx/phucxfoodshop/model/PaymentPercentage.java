package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPercentage {
    private String status;
    private BigDecimal percentage;
}
