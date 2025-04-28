package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ProductDTO {
    Integer productId;
    String productName;
    BigDecimal unitPrice;
    Integer unitsInStock;
    String picture;
    String description;

    CategoryDTO category;


}
