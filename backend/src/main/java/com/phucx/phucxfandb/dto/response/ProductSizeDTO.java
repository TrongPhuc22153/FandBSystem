package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductSizeDTO {
    ProductDTO product;
    Integer height;
    Integer length;
    Integer weight;
    Integer width;

}
