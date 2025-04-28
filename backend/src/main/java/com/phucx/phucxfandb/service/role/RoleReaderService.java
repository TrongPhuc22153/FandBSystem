package com.phucx.phucxfandb.service.role;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.response.RoleDTO;
import com.phucx.phucxfandb.entity.Role;

import java.util.List;

public interface RoleReaderService {
    RoleDTO getRoleDTOByName(RoleName roleName);
    Role getRoleEntityByName(RoleName roleName);
    List<Role> getRoleEntitiesByName(List<RoleName> roleNames);

}
