package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    @EntityGraph(attributePaths = {"roles", "profile"})
    @Transactional(readOnly = true)
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "profile"})
    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);

    @Transactional(readOnly = true)
    boolean existsByUsername(String username);

    @Transactional(readOnly = true)
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Page<User> findByRolesRoleName(RoleName roleName, Pageable pageable);
}
