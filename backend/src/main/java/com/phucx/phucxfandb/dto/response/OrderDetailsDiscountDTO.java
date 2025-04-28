package com.phucx.phucxfandb.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Value
public class OrderDetailsDiscountDTO {
    LocalDateTime appliedDate;
    Integer discountPercent;
    DiscountDTO discount;
}
