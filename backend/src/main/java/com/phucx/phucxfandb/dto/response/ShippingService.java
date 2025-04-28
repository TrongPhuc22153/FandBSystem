package com.phucx.phucxfandb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingService {
    private Integer serviceId;
    private String shortName;
    private Integer serviceTypeId;
}
