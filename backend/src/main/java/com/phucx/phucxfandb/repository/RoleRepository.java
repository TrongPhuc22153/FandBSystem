package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByRoleNameAndIsDeletedFalse(RoleName roleName);

    List<Role> findByRoleNameInAndIsDeletedFalse(List<RoleName> roleNames);

    Set<Role> findByRoleNameInAndIsDeletedFalse(Set<RoleName> roleNames);

    boolean existsByRoleName(RoleName roleName);

    boolean existsByRoleNameIn(List<RoleName> roleNames);

    Set<Role> findByIsDeletedFalse();
    
}
