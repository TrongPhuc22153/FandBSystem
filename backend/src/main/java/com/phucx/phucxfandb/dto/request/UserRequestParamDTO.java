package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.constant.WebConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestParamDTO {
    private String username;
    private String email;
    private RoleName roleName;
    private Boolean enabled;
    private String field = "username";
    private Sort.Direction direction = Sort.Direction.ASC;
    private int page = WebConstant.PAGE_NUMBER;
    private int size = WebConstant.PAGE_SIZE;
}
