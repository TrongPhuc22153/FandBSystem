package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.dto.event.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConfig {
    private final JavaMailSender javaMailSender;

    @Async
    @EventListener
    public void EmailHandler(EmailEvent emailEvent){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailEvent.getFromEmail());
        message.setTo(emailEvent.getToEmail());
        message.setSubject(emailEvent.getSubject());
        message.setText(emailEvent.getText());
        javaMailSender.send(message);
    }

}
