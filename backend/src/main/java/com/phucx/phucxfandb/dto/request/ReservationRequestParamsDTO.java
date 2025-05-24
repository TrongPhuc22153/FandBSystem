package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestParamsDTO extends PaginationParamsDTO {
    private String field = "startTime";
    private Sort.Direction direction = Sort.Direction.DESC;
    private ReservationStatus status;
}
