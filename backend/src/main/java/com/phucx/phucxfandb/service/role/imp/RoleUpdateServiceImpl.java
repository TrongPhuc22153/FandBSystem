package com.phucx.phucxfandb.service.role.imp;

import com.phucx.phucxfandb.dto.request.RequestRoleDTO;
import com.phucx.phucxfandb.dto.response.RoleDTO;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.mapper.RoleMapper;
import com.phucx.phucxfandb.repository.RoleRepository;
import com.phucx.phucxfandb.service.role.RoleUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleUpdateServiceImpl implements RoleUpdateService {
    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    @Override
    @Modifying
    @Transactional
    public RoleDTO createRole(RequestRoleDTO requestRoleDTO) {
        log.info("createRole(requestRoleDTO={})", requestRoleDTO);
        if(roleRepository.existsByRoleName(requestRoleDTO.getRoleName())){
            throw new EntityExistsException("Role " + requestRoleDTO.getRoleName().name() + " already exists");
        }
        Role newRole = mapper.toRole(requestRoleDTO);
        Role savedRole = roleRepository.save(newRole);
        return mapper.toRoleDTO(savedRole);
    }

    @Override
    @Modifying
    @Transactional
    public Set<RoleDTO> createRoles(Set<RequestRoleDTO> requestRoleDTOs) {
        log.info("createRoles(requestRoleDTOs={})", requestRoleDTOs);
        if(roleRepository.existsByRoleNameIn(requestRoleDTOs.stream().map(RequestRoleDTO::getRoleName).toList())){
            throw new EntityExistsException("Roles already exists");
        }
        Collection<Role> newRoles = requestRoleDTOs.stream().map(mapper::toRole).toList();


        newRoles = roleRepository.saveAll(newRoles);
        return newRoles.stream().map(mapper::toRoleDTO)
                .collect(Collectors.toSet());
    }
}
