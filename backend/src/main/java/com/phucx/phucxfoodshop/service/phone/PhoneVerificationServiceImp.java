package com.phucx.phucxfoodshop.service.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.service.user.UserProfileService;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.model.UserProfile;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhoneVerificationServiceImp implements PhoneVerificationService {
    @Autowired
    private UserProfileService userProfileService;
    @Value("${twilio.client-id}")
    private String clientID;
    @Value("${twilio.client-secret}")
    private String clientsecret;
    @Value("${twilio.service-sid}")
    private String serviceSID;

    private String APPROVED = "approved";

    @Override
    public String generateOTP(String phone) {
        log.info("generateOTP(phone={})", phone);
        Twilio.init(clientID, clientsecret);
        Verification verification = Verification.creator(serviceSID, phone, "sms").create();
        log.info("Verification: {}", verification.getStatus());

        return verification.getStatus();
    }

    @Override
    public ResponseFormat verifyOTP(String otp, String phone, String userID) throws UserNotFoundException {
        log.info("verifyOTP(otp={}, phone={}, userID={})", otp, phone, userID);
        Twilio.init(clientID, clientsecret);
        VerificationCheck verificationCheck = VerificationCheck
            .creator(serviceSID)
            .setTo(phone)
            .setCode(otp).create();
        log.info("Verification: {}", verificationCheck.getStatus());

        ResponseFormat responseFormat = new ResponseFormat();
        // verify phone otp
        if(verificationCheck.getStatus().equalsIgnoreCase(APPROVED)){
            UserProfile userProfile = userProfileService.getUserProfile(userID);
            Boolean result = userProfileService.updatePhoneVerification(userProfile.getProfileID(), true);
            responseFormat.setStatus(result);
        }else{
            responseFormat.setStatus(false); 
        }
        log.info("VerifyOTP responseformat: {}", responseFormat);
        
        return responseFormat;
    }
    
}
