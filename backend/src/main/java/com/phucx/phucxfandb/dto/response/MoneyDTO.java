package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class MoneyDTO {
    String currencyCode;
    BigDecimal value;
}
