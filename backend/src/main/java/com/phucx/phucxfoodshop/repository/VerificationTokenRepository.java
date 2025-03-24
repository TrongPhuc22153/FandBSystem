package com.phucx.phucxfoodshop.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.phucx.phucxfoodshop.model.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    @Procedure("SaveVerificationtToken")
    void saveVerificationtToken(String id, String token, String username, String type, Date expiryDate);
}
