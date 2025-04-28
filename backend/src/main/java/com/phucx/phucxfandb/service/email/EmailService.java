package com.phucx.phucxfandb.service.email;

public interface EmailService {
    String PASSWORD_RESET_SUBJECT = "YOUR PASSWORD HAS BEEN RESET";

    void sendMessage(String to, String subject, String text);
    void sendVerificationEmail(String to, String username, String baseUrl);
    Boolean validateEmail(String token);

}
