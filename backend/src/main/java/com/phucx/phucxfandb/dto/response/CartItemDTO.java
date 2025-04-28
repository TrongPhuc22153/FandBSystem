package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CartItemDTO {
    ProductDTO product;
    Integer quantity;
    BigDecimal unitPrice;
}
