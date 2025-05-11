package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.WebConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestParamDTO {
    private String searchValue;
    private Long categoryId;
    private Boolean isFeatured;
    private Boolean isDeleted;
    private String field = "productId";
    private Sort.Direction direction = Sort.Direction.ASC;
    private int page = WebConstant.PAGE_NUMBER;
    private int size = WebConstant.PAGE_SIZE;
}
