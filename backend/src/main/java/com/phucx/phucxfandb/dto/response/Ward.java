package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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
