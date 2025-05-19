package com.phucx.phucxfandb.service.email;

public interface EmailService {
    void sendMessage(String toEmail, String subject, String text);
    void sendResetPassword(String toEmail);
}
