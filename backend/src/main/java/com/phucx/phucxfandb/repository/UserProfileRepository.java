package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String>{
    @EntityGraph(attributePaths = {"user"})
    Optional<UserProfile> findByUserUsername(String username);

    @EntityGraph(attributePaths = {"user"})
    Optional<UserProfile> findByUserUserId(String userId);
}
