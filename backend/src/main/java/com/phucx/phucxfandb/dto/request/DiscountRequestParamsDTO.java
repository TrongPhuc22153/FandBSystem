package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequestParamsDTO extends PaginationParamsDTO {
    private String field = "discountId";
}
