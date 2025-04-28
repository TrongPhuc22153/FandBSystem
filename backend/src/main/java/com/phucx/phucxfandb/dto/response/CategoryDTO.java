package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoryDTO {
    Integer categoryId;
    String categoryName;
    String description;
    String picture;
}
