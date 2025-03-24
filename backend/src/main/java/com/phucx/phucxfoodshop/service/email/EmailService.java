package com.phucx.phucxfoodshop.service.email;

public interface EmailService {
    public void sendMessage(String to, String subject, String text);
    public void sendVerificationEmail(String to, String username, String baseUrl);
    public Boolean validateEmail(String token);

}
