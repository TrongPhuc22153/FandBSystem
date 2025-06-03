package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TableSummaryDTO {
    Long occupied;
    Long unoccupied;
    Long cleaning;
    Long reserved;
    Long total;
}
