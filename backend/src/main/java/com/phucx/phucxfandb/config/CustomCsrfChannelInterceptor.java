package com.phucx.phucxfandb.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class CustomCsrfChannelInterceptor implements ChannelInterceptor{

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        return message;
    }
    
}
