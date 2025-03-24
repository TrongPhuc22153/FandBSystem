package com.phucx.phucxfoodshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class District {
    private Integer districtID;
    private Integer provinceID;
    private String districtName;
    private Integer supportType;
    private List<String> nameExtension;
    private Boolean canUpdateCOD;
    private Integer status;
    private String createdDate;
    private String updatedDate;
}
