package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestParamsDTO extends PaginationParamsDTO {
    private String field = "productName";
    private String search;
    private Long categoryId;
    private Boolean isFeatured;
    private Boolean isDeleted;
}
