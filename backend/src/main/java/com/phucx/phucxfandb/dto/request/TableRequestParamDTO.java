package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.TableStatus;
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
public class TableRequestParamDTO {
    private String field = "tableNumber";
    private Sort.Direction direction = Sort.Direction.ASC;
    private int page = WebConstant.PAGE_NUMBER;
    private int size = WebConstant.PAGE_SIZE;
    private Integer tableNumber;
    private TableStatus status;
    private Boolean isDeleted;
}
