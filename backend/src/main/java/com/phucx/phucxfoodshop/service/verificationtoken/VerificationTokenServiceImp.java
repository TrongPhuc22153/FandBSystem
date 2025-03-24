package com.phucx.phucxfoodshop.service.verificationtoken;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.JwtType;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.VerificationToken;
import com.phucx.phucxfoodshop.repository.VerificationTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerificationTokenServiceImp implements VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void saveVerificationToken(String id, String token, String username, JwtType type, Date expiryDate) {
        log.info("saveVerificationToken(id={}, token={}, username={}, type={}, date={})", 
            id, token, username, type, expiryDate);
        String typeStr = type.name().toLowerCase();
        verificationTokenRepository.saveVerificationtToken(id, token, username, typeStr, expiryDate);
    }

    @Override
    public VerificationToken getVerficiaToken(String id) throws NotFoundException {
        log.info("getVerficiaToken(id={})", id);
        return verificationTokenRepository.findById(id).orElseThrow(
            ()-> new NotFoundException("Token " + id + " does not found"));
    }
    
}
