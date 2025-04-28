package com.phucx.phucxfandb.dto.response;

import lombok.*;

@Value
@Builder
public class DistrictTransfer {
    Integer toDistrictId;
    Integer fromDistrictId;
}
