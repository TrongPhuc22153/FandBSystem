package com.phucx.phucxfandb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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
