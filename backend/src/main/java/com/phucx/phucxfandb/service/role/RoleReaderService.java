package com.phucx.phucxfandb.service.role;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.dto.response.RoleDTO;
import com.phucx.phucxfandb.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleReaderService {
    Role getRoleEntityByName(RoleName roleName);
    List<Role> getRoleEntitiesByName(List<RoleName> roleNames);
    Set<Role> getRoleEntitiesByName(Set<RoleName> roleNames);
    Set<RoleDTO> getRoles();

}
