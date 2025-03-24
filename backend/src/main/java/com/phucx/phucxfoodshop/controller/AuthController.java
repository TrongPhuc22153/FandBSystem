package com.phucx.phucxfoodshop.controller;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.config.ServerURLProperties;
import com.phucx.phucxfoodshop.exceptions.InvalidTokenException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserAuthenticationException;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserPasswordException;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.model.UserAuthentication;
import com.phucx.phucxfoodshop.model.UserChangePasswordToken;
import com.phucx.phucxfoodshop.model.UserInfo;
import com.phucx.phucxfoodshop.model.UserRegisterInfo;
import com.phucx.phucxfoodshop.service.email.EmailService;
import com.phucx.phucxfoodshop.service.user.UserPasswordService;
import com.phucx.phucxfoodshop.service.user.UserProfileService;
import com.phucx.phucxfoodshop.service.user.UserSysDetailsService;
import com.phucx.phucxfoodshop.utils.ServerUrlUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final UserProfileService userProfileService;
    private final UserSysDetailsService userService;
    private final UserPasswordService userPasswordService;
    private final EmailService emailService;
    private final ServerURLProperties serverURLProperties;

    @Operation(tags = {"public", "get", "login"}, summary = "Login endpoint",
        description = "Send a get request to /login endpoint as a Basic Authentication")
    @GetMapping("/login")
    public ResponseEntity<UserAuthentication> login(Authentication authentication) throws UserNotFoundException{
        UserAuthentication userAuthentication = userProfileService
            .getUserAuthentication(authentication);
        
        return ResponseEntity.ok().body(userAuthentication);
    }

    @PostMapping("/register")
    @Operation(tags = {"public", "post", "register"}, summary = "Customer register endpoint")
    public ResponseEntity<ResponseFormat> register(HttpServletRequest request, 
        @RequestBody UserRegisterInfo userRegisterInfo) throws UserAuthenticationException{
        // create customer
        Boolean status = userService.registerCustomer(userRegisterInfo);
        String email = userRegisterInfo.getEmail();
        String username = userRegisterInfo.getUsername();
        //  send verification email
        emailService.sendVerificationEmail(email, username, ServerUrlUtils.getBaseUrl(request));
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setMessage("A message has been sent to your email");
        responseFormat.setStatus(status);
        return ResponseEntity.ok().body(responseFormat);
    }

    @GetMapping("/verify")
    @Operation(tags = {"public", "get", "verify token"}, summary = "User verification email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token){
        Boolean result = emailService.validateEmail(token);
        String redirectUrl = serverURLProperties.getUiUrl() + "/auth";
        if(result){
            redirectUrl += "?status=true";
        }else{
            redirectUrl += "?status=false";
        }
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(redirectUrl)).build();
    }

    @Operation(summary = "Send a email to user", tags = {"public", "get", "forgot password"},
        description = "Send a reset password link to user via email")
    @PostMapping("/forgot")
    public ResponseEntity<ResponseFormat> forgotEmail(HttpServletRequest request,
        @RequestParam(name = "email") String email) {

        Boolean result = userPasswordService.sendResetPasswordLink(serverURLProperties.getUiUrl(), email);
        ResponseFormat responseFormat = new ResponseFormat(result);
        return ResponseEntity.ok().body(responseFormat);
    }

    @Operation(summary = "Verfiy a reset password token", tags = {"public", "get", "verify token"})
    @GetMapping("/verifyReset")
    public ResponseEntity<UserInfo> verifyResetToken(
        @RequestParam(name = "token", required = true) String token
    ) throws NotFoundException, InvalidTokenException {
        UserInfo userInfo = userPasswordService.getUserByResetToken(token);
        return ResponseEntity.ok().body(userInfo);
    }

    @Operation(summary = "Reset user's password", tags = {"public", "post", "change password"})
    @PostMapping("/reset")
    public ResponseEntity<ResponseFormat> resetPassword(
        @RequestBody UserChangePasswordToken userChangePasswordToken
    ) throws UserPasswordException, InvalidTokenException {
        Boolean status = userPasswordService.resetUserPassword(userChangePasswordToken);
        ResponseFormat responseFormat = new ResponseFormat(status);
        return ResponseEntity.ok().body(responseFormat);
    }
}
