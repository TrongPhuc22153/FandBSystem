package com.phucx.phucxfoodshop.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserPasswordException;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.model.UserAuthentication;
import com.phucx.phucxfoodshop.model.UserChangePassword;
import com.phucx.phucxfoodshop.service.phone.PhoneVerificationService;
import com.phucx.phucxfoodshop.service.user.UserPasswordService;
import com.phucx.phucxfoodshop.service.user.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserProfileService userProfileService;
    private final PhoneVerificationService phoneVerificationService;
    private final UserPasswordService userPasswordService;

    @Operation(summary = "Get user information", 
        tags = {"get", "user"},
        description = "Get username, roles of a user")
    @GetMapping("/userInfo")
    public ResponseEntity<UserAuthentication> getUserInfo(Authentication authentication) throws UserNotFoundException{
        log.info("getUserInfo(username={})", authentication.getName());
        UserAuthentication user = userProfileService.getUserAuthentication(authentication);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "Generate OTP code for user's phone number", 
        tags = {"post", "user"}, 
        description = "Generate OTP code for user's phone number")
    @PostMapping("/phone/generateOTP")
    public ResponseEntity<Map<String, String>> generateOTP(@RequestParam(name = "phone") String phone) {
        String vnphone = "+" + phone.substring(1);
        String status = phoneVerificationService.generateOTP(vnphone);
        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "Verify user's phone number", 
        tags = {"post", "user"}, 
        description = "Verify user's phone number")
    @PostMapping("/phone/verifyOTP")
    public ResponseEntity<ResponseFormat> verifyUserOTP(
        @RequestParam(name = "otp") String otp, 
        @RequestParam(name = "phone") String phone,
        Authentication authentication) throws Exception {
        
        String vnphone = "+" + phone.substring(1);
        ResponseFormat responseFormat = phoneVerificationService.verifyOTP(otp, vnphone, authentication.getName());
        return ResponseEntity.ok().body(responseFormat);
    }

    @Operation(summary = "Update user password", tags = {"post", "user"})
    @PostMapping("/changePassword")
    public ResponseEntity<ResponseFormat> changePassword(
        @RequestBody UserChangePassword userChangePassword
    ) throws UserPasswordException{
        Boolean result = userPasswordService.changePassword(userChangePassword);
        ResponseFormat responseFormat = new ResponseFormat(result);
        return ResponseEntity.ok().body(responseFormat);
    }
}
