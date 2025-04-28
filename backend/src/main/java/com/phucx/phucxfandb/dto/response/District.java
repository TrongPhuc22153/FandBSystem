package com.phucx.phucxfandb.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Value
public class District {
    Integer districtID;
    Integer provinceID;
    String districtName;
    Integer supportType;
    List<String> nameExtension;
    Boolean canUpdateCOD;
    Integer status;
    String createdDate;
    String updatedDate;
}
