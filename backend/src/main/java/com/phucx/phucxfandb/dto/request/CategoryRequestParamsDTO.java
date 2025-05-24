package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestParamsDTO extends PaginationParamsDTO {
    private String field = "categoryName";
    private Boolean isDeleted;
    private String search;
}
