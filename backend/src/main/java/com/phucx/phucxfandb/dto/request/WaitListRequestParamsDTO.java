package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.WaitListStatus;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitListRequestParamsDTO extends PaginationParamsDTO {
    private String field = "createdAt";
    private WaitListStatus status = WaitListStatus.WAITING;
}
