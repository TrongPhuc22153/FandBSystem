package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
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
