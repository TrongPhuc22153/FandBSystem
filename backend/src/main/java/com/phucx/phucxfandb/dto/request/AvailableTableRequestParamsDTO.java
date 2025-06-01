package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTableRequestParamsDTO extends PaginationParamsDTO {
    private String field = "tableNumber";
    private String search;
    private Integer tableNumber;
    private LocalDate date;
    private LocalTime time;
}
