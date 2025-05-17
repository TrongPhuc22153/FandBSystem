package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    @EntityGraph(attributePaths = {"roles", "profile"})
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"roles", "profile"})
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Page<User> findByRolesRoleName(RoleName roleName, Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"roles"})
    Page<User> findAll(@Nullable Specification<User> spec, @NonNull Pageable pageable);
}
