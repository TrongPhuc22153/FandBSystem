package com.phucx.phucxfoodshop.service.user.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.JwtType;
import com.phucx.phucxfoodshop.exceptions.InvalidTokenException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserPasswordException;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.UserChangePassword;
import com.phucx.phucxfoodshop.model.UserChangePasswordToken;
import com.phucx.phucxfoodshop.model.UserInfo;
import com.phucx.phucxfoodshop.model.VerificationToken;
import com.phucx.phucxfoodshop.service.email.EmailService;
import com.phucx.phucxfoodshop.service.jwt.JwtService;
import com.phucx.phucxfoodshop.service.user.UserPasswordService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.service.user.UserSysDetailsService;
import com.phucx.phucxfoodshop.service.verificationtoken.VerificationTokenService;
import com.phucx.phucxfoodshop.utils.RandomStringGeneratorUtils;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserPasswordServiceImp implements UserPasswordService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSysDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private VerificationTokenService verificationTokenService;

    private final Integer PASSWORD_LENGTH = 10;
    private final Long RESET_TOKEN_TIME = 300000L;
    private final String PASSWORD_RESET_SUBJECT = "PASSWORD RESET";
    private final String RESET_URI = "/auth/reset";

    @Override
    public Boolean resetUserPasswordRandom(String userID) {
        log.info("resetUserPasswordRandom(userID={})", userID);
        User user = userService.getUserById(userID);
        String password = RandomStringGeneratorUtils.generateRandomString(PASSWORD_LENGTH); 
        String text = "Your new password is: " + password;
        emailService.sendMessage(user.getEmail(), PASSWORD_RESET_SUBJECT, text);

        return userDetailsService.updateUserPassword(userID, password);
    }

    @Override
    public Boolean changePassword(UserChangePassword userChangePassword) throws UserPasswordException {
        log.info("changePassword(userID={}, email={})", userChangePassword.getUserID(), userChangePassword.getEmail());
        User user = userService.getUserById(userChangePassword.getUserID());
        String password = userChangePassword.getPassword();
        String newPassword = userChangePassword.getNewPassword();
        String newConfirmedPassword = userChangePassword.getConfirmNewPassword();
        // check password
        if(!newConfirmedPassword.equals(newPassword))
            throw new UserPasswordException("Password and confirm password do not match!");
        if(newPassword.equals(password))
            throw new UserPasswordException("Your new password and old password can not match!");
        if(!userChangePassword.getEmail().equals(user.getEmail()))
            throw new UserPasswordException("Your email does not match with your original email!");
        if(!userDetailsService.checkPassword(user.getPassword(), password))
            throw new UserPasswordException("Your old password is wrong!");
        // update password
        return userDetailsService.updateUserPassword(user.getUserID(), newPassword);
    }

    @Override
    public Boolean sendResetPasswordLink(String baseUrl, String email) {
        log.info("sendResetPasswordLink(baseUrl, email={})", baseUrl, email);
        try {
            // get user
            User user = userService.getUserByEmail(email);
            // create a reset password link
            String token = this.generateResetToken(email, user.getUsername());
            String resetUrl = baseUrl + RESET_URI + "?token=" + token;
            String text = "Your reset password link is: " + resetUrl;
            emailService.sendMessage(user.getEmail(), PASSWORD_RESET_SUBJECT, text);

            return true;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }

    // generate reset token
    private String generateResetToken(String email, String username){
        String jId = UUID.randomUUID().toString();
        Date issueat = new Date(System.currentTimeMillis());
        Date expiredDate = new Date(System.currentTimeMillis() + RESET_TOKEN_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", JwtType.RESET_PASSWORD.getValue());
        claims.put("email", email);

        String token = this.jwtService.createToken(
            jId, claims, username, issueat, expiredDate);

        // save token
        this.verificationTokenService.saveVerificationToken(jId, token, 
            username, JwtType.RESET_PASSWORD, expiredDate);

        return token;
    }

    // validate reset password token
    private Boolean validateResetPasswordToken(String token, String username, String useremail, String tokenType){
        log.info("validateResetPasswordToken(token={}, username={}, email={}, type={})",
             token, username, useremail, tokenType);
        Claims claims = this.jwtService.extractAllClaims(token);
        String type = claims.get("type").toString();
        String name = this.jwtService.extractUsername(token);
        String email = claims.get("email").toString();
        if(this.jwtService.isTokenExpired(token))
            return false;
        if(!tokenType.equalsIgnoreCase(type))
            return false;
        if(!name.equals(username))
            return false;
        if(!useremail.equals(email))
            return false;    
        return true;
    }

    @Override
    public UserInfo getUserByResetToken(String token) throws NotFoundException, InvalidTokenException{
        log.info("getUserByResetToken(token={})", token);
        Claims claims = this.jwtService.extractAllClaims(token);
        String jId = claims.getId();
        VerificationToken verificationToken = verificationTokenService.getVerficiaToken(jId);
        User user = userService.getUserById(verificationToken.getUserID());
        // validate token
        Boolean status = this.validateResetPasswordToken(token, user.getUsername(), 
            user.getEmail(), verificationToken.getType());
        if(!status) throw new InvalidTokenException("Invalid token!");
        // extract userinfo
        return new UserInfo(user.getUserID(), user.getUsername(), user.getEmail());
    }

    @Override
    public Boolean resetUserPassword(UserChangePasswordToken userChangePasswordToken) 
    throws UserPasswordException, InvalidTokenException {
        log.info("resetUserPassword(userID={}, email={})", 
            userChangePasswordToken.getUserID(), 
            userChangePasswordToken.getEmail());
        Boolean status = this.validateResetPasswordToken(
            userChangePasswordToken.getToken(), 
            userChangePasswordToken.getUsername(), 
            userChangePasswordToken.getEmail(), 
            JwtType.RESET_PASSWORD.getValue());
        if(!status) throw new InvalidTokenException("Invalid token!");
        return this.changePasswordForForgot(userChangePasswordToken);
    }

    // change password for forgot
    private Boolean changePasswordForForgot(UserChangePassword userChangePassword) throws UserPasswordException {
        log.info("changePassword(userID={}, email={})", userChangePassword.getUserID(), userChangePassword.getEmail());
        User user = userService.getUserById(userChangePassword.getUserID());
        String newPassword = userChangePassword.getNewPassword();
        String newConfirmedPassword = userChangePassword.getConfirmNewPassword();
        // check password
        if(!newConfirmedPassword.equals(newPassword))
            throw new UserPasswordException("Password and confirm password do not match!");
        if(!userChangePassword.getEmail().equals(user.getEmail()))
            throw new UserPasswordException("Your email does not match with your original email!");
        // update password
        return userDetailsService.updateUserPassword(user.getUserID(), newPassword);
    }
}
