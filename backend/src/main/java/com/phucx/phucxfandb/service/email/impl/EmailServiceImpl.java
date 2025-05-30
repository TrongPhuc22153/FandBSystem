package com.phucx.phucxfandb.service.email.impl;

import com.phucx.phucxfandb.constant.EmailConstants;
import com.phucx.phucxfandb.enums.JwtType;
import com.phucx.phucxfandb.service.email.EmailService;
import com.phucx.phucxfandb.service.jwt.JwtEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    private final JwtEmailService jwtEmailService;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${phucx.allowed-url}")
    private String frontendUrl;

    @Value("${email.forgot-password.uri}")
    private String resetPasswordUri;
    
    @Override
    public void sendMessage(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendResetPassword(String toEmail) {
        String token = jwtEmailService.generateToken(toEmail, JwtType.RESET_PASSWORD);
        String resetPasswordUrl = String.format("%s%s?token=%s", frontendUrl, resetPasswordUri, token);
        String message = String.format(EmailConstants.RESET_PASSWORD_TEXT_TEMPLATE, toEmail, resetPasswordUrl);
        this.sendMessage(toEmail, EmailConstants.RESET_PASSWORD_SUBJECT, message);
    }

    @Override
    public void sendPassword(String toEmail, String firstname, String lastname, String username, String password) {
        String message = String.format(EmailConstants.PASSWORD_TEXT_TEMPLATE, firstname + " " + lastname, username, password);
        this.sendMessage(toEmail, EmailConstants.PASSWORD_SUBJECT, message);
    }
}
