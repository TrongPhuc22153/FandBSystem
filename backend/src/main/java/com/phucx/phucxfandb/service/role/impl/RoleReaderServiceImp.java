package com.phucx.phucxfandb.service.role.impl;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.dto.response.RoleDTO;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.RoleMapper;
import com.phucx.phucxfandb.repository.RoleRepository;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RoleReaderServiceImp implements RoleReaderService {
    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    @Override
    public Role getRoleEntityByName(RoleName roleName) {
        log.info("getRoleEntityByName(roleName={})", roleName);
        return roleRepository.findByRoleNameAndIsDeletedFalse(roleName)
                .orElseThrow(()-> new NotFoundException("Role", roleName.name()));
    }

    @Override
    public List<Role> getRoleEntitiesByName(List<RoleName> roleNames) {
        log.info("getRoleEntitiesByName(roleNames={})", roleNames);
        List<Role> roles = roleRepository.findByRoleNameInAndIsDeletedFalse(roleNames);
        if(roles.size()!=roleNames.size())
            throw new NotFoundException("Role not found");
        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> getRoleEntitiesByName(Set<RoleName> roleNames) {
        log.info("getRoleEntitiesByName(roleNames={})", roleNames);
        Set<Role> roles = roleRepository.findByRoleNameInAndIsDeletedFalse(roleNames);
        if(roles.size()!=roleNames.size())
            throw new NotFoundException("Role not found");
        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RoleDTO> getRoles() {
        log.info("getRoles()");
        return roleRepository.findByIsDeletedFalse()
                .stream()
                .map(mapper::toRoleDTO)
                .collect(Collectors.toSet());
    }
}
