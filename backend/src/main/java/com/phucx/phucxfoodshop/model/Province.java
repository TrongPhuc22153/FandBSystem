package com.phucx.phucxfoodshop.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Province {
    private Integer provinceID;
    private String provinceName;
    private List<String> nameExtension;
    private String createdAt;
    private String updatedAt;
    private Boolean canUpdateCOD;
    private Integer status;
}
