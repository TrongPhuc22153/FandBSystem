package com.phucx.phucxfoodshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.UserVerification;


@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, String>{
    @Procedure("UpdatePhoneVerification")
    public Boolean updatePhoneVerification(String profileID, Boolean phoneVerification);

    @Procedure("updateProfileVerification")
    public Boolean updateProfileVerification(String profileID, Boolean profileVerification);

    @Query("""
        SELECT uv FROM UserVerification uv \
        WHERE uv.profileID=?1
            """)
    public Optional<UserVerification> findByProfileID(String profileID);

    @Query("""
        SELECT uv \
        FROM UserVerification uv JOIN UserProfile up on uv.profileID=up.profileID \
        WHERE up.userID=?1
            """)
    public Optional<UserVerification> findByUserID(String userID);
}
