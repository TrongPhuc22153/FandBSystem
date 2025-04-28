package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class CartDTO {
    String id;
    List<CartItemDTO> cartItems;
    BigDecimal totalPrice;
}
