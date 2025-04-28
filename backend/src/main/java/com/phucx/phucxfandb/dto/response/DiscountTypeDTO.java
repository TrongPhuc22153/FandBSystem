package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DiscountTypeDTO {
    Long discountTypeId;
    String discountType;
}
