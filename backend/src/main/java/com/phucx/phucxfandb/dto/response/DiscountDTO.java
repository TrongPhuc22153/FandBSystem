package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DiscountDTO {
    String discountId;
    Integer discountPercent;
    String discountCode;
    DiscountTypeDTO discountType;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean active;
}
