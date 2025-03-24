package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfo{
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
