package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShippingInfo {
    private Integer serviceId;
    private Integer insuranceValue;
    private String coupon;
    private String toWardCode;
    private Integer toDistrictId;
    private Integer fromDistrictId;
    private Integer height;
    private Integer length;
    private Integer weight;
    private Integer width;
}
