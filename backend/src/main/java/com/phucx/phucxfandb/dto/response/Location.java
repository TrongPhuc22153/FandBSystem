package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    private String address;
    private String city;
    private String district;
    private String ward;

    private Integer cityId;
    private Integer districtId;
    private String wardCode;
}
