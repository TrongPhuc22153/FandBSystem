package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.RoleName;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestRoleDTO {
    private RoleName roleName;
}
