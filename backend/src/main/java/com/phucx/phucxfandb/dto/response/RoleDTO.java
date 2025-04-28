package com.phucx.phucxfandb.dto.response;

import com.phucx.phucxfandb.constant.RoleName;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RoleDTO {
    RoleName roleName;
}
