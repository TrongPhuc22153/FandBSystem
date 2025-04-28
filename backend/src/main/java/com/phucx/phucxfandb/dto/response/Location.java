package com.phucx.phucxfandb.dto.response;

import lombok.*;

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
