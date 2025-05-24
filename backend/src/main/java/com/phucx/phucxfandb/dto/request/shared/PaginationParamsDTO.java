package com.phucx.phucxfandb.dto.request.shared;

import com.phucx.phucxfandb.constant.WebConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationParamsDTO {
    private int page = WebConstant.PAGE_NUMBER;
    private int size = WebConstant.PAGE_SIZE;
    private Sort.Direction direction = Sort.Direction.ASC;
    private String field;
}
