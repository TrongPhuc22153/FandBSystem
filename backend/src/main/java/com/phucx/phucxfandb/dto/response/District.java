package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
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
