package com.phucx.phucxfoodshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ward {
    private String wardCode;
    private Integer districtID;
    private String wardName;
    private List<String> nameExtension;
    private Boolean canUpdateCOD;
    private Integer supportType;
    private Integer status;
    private String createdDate;
    private String updatedDate;
}
