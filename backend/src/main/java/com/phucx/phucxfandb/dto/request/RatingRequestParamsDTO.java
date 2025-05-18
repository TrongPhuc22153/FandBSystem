package com.phucx.phucxfandb.dto.request;

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
public class RatingRequestParamsDTO {
    private String field = "lastModifiedAt";
    private Sort.Direction direction = Sort.Direction.DESC;
    private int page = WebConstant.PAGE_NUMBER;
    private int size = WebConstant.PAGE_SIZE;
}
