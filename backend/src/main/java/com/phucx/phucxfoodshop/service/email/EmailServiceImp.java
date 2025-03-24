package com.phucx.phucxfoodshop.service.email;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.EmailVerified;
import com.phucx.phucxfoodshop.constant.JwtType;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.VerificationToken;
import com.phucx.phucxfoodshop.service.jwt.JwtService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.service.verificationtoken.VerificationTokenService;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImp implements EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private VerificationTokenService verificationTokenService;

    private final Long EXPIRATION_TIME = 300000L;
    private final String VERIFFY_EMAIL_URI = "/verify";
    @Value("${spring.mail.username}")
    private String email;

    @Override
    public void sendMessage(String to, String subject, String text) {
        log.info("sendMessage(to={}, subject={}, text={})", to, subject, text);
        // send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply <"+ email +">");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendVerificationEmail(String to, String username, String baseUrl) {
        log.info("sendVerificationEmail(to={}, username={}, baseUrl={})", to, username, baseUrl);
        // generate a token
        String id = UUID.randomUUID().toString();
        String token = this.generateEmailToken(id, to, username);
        // send message
        String url = baseUrl + this.VERIFFY_EMAIL_URI + "?token=" + token;
        String message = "Click the link below to verify your email address \n " + url;
        sendMessage(to, "Verify email", message);
    }

    @Override
    public Boolean validateEmail(String token) {
        log.info("validateEmail(token={})", token);
        try {
            Claims claims = this.jwtService.extractAllClaims(token);
            String name = this.jwtService.extractUsername(token);
            String type = claims.get("type").toString();
            String email = claims.get("email").toString();
            String jId = claims.getId();
            // validate token
            VerificationToken verificationToken = verificationTokenService.getVerficiaToken(jId);
            User user = userService.getUserById(verificationToken.getUserID());
            if(!name.equalsIgnoreCase(user.getUsername())){
                return false;
            }
            if(this.jwtService.isTokenExpired(token)){
                return false;
            }
            if(!type.equalsIgnoreCase(JwtType.VERIFY_EMAIL.name())){
                return false;
            }
            if(!email.equalsIgnoreCase(user.getEmail())){
                return false;
            }
            // update email verified status
            userService.updateEmailVerification(user.getUsername(), EmailVerified.YES.getValue());
            return true;
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        } catch (UserNotFoundException e){
            log.error("Error: {}", e.getMessage());
            return false;
        } catch (Exception e){
            log.error("Error: ", e.getMessage());
            return false;
        }
        
    }

    // generate an email token
    private String generateEmailToken(String id, String email, String username) {
        log.info("generateEmailToken(email={}, username={})", email, username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("type", JwtType.VERIFY_EMAIL);
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis()+EXPIRATION_TIME);
        // create a token
        String token = this.jwtService.createToken(id, claims, username, issuedAt, expiryDate);
        // save token 
        verificationTokenService.saveVerificationToken(id, token, username, JwtType.VERIFY_EMAIL, expiryDate);
        return token;
    }
    
}
