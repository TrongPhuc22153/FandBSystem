package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    @Transactional(readOnly = true)
    Optional<Role> findByRoleNameAndIsDeletedFalse(RoleName roleName);

    @Transactional(readOnly = true)
    List<Role> findByRoleNameInAndIsDeletedFalse(List<RoleName> roleNames);

    Set<Role> findByRoleNameInAndIsDeletedFalse(Set<RoleName> roleNames);

    @Transactional(readOnly = true)
    boolean existsByRoleName(RoleName roleName);

    @Transactional(readOnly = true)
    boolean existsByRoleNameIn(List<RoleName> roleNames);

    Set<Role> findByIsDeletedFalse();
    
}
