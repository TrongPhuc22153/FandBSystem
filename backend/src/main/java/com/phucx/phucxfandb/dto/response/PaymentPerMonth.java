package com.phucx.phucxfandb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPerMonth {
    private Integer month;
    private BigDecimal total;
}
