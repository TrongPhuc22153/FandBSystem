package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableRequestParamsDTODTO extends PaginationParamsDTO {
    private String field = "tableNumber";
    private String search;
    private Integer tableNumber;
    private TableStatus status;
    private Boolean isDeleted;
}
