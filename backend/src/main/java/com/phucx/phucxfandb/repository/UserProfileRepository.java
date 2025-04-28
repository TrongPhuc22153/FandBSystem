package com.phucx.phucxfandb.repository;

import com.phucx.phucxfandb.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String>{

}
