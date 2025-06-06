package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestRoleDTO;
import com.phucx.phucxfandb.dto.response.RoleDTO;
import com.phucx.phucxfandb.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Role toRole(RoleDTO roleDTO);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Role toRole(RequestRoleDTO requestRoleDTO);

}
