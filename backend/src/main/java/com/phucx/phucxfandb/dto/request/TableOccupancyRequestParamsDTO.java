package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableOccupancyRequestParamsDTO extends PaginationParamsDTO {
    private String field = "createdAt";
    private TableOccupancyStatus status = TableOccupancyStatus.WAITING;
}
