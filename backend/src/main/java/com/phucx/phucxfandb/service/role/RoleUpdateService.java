package com.phucx.phucxfandb.service.role;

import com.phucx.phucxfandb.dto.request.RequestRoleDTO;
import com.phucx.phucxfandb.dto.response.RoleDTO;

import java.util.Set;

public interface RoleUpdateService {
    RoleDTO createRole(RequestRoleDTO requestRoleDTO);
    Set<RoleDTO> createRoles(Set<RequestRoleDTO> requestRoleDTOs);

}
