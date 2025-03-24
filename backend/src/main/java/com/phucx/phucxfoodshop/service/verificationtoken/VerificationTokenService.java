package com.phucx.phucxfoodshop.service.verificationtoken;

import java.util.Date;

import com.phucx.phucxfoodshop.constant.JwtType;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.VerificationToken;

public interface VerificationTokenService {
    public void saveVerificationToken(String id, String token, String username, JwtType type, Date expiryDate);
    public VerificationToken getVerficiaToken(String id) throws NotFoundException;

}
