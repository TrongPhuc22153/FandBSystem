package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.constant.WebConstant;
import com.phucx.phucxfandb.dto.request.shared.PaginationParamsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestParamsDTO extends PaginationParamsDTO {
    private String field = "username";
    private String search;
    private String username;
    private String email;
    private RoleName roleName;
    private Boolean enabled;
}
