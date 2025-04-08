package com.phucx.phucxfoodshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String address;
    private String city;
    private String district;
    private String ward;

    private Integer cityId;
    private Integer districtId;
    private String wardCode;
}
