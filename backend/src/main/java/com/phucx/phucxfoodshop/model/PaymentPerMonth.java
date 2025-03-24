package com.phucx.phucxfoodshop.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPerMonth {
    private Integer month;
    private BigDecimal total;
}
