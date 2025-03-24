package com.phucx.phucxfoodshop.service.phone;

import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.ResponseFormat;

public interface PhoneVerificationService {
    public String generateOTP(String phone);
    public ResponseFormat verifyOTP(String otp, String phone, String userID) throws UserNotFoundException;
}
