package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class OrderDetailDTO {
    ProductDTO product;
    List<OrderDetailsDiscountDTO> discounts;

    Integer quantity;
    BigDecimal unitPrice;
}
