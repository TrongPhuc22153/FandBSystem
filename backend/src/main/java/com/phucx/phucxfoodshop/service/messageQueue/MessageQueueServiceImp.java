package com.phucx.phucxfoodshop.service.messageQueue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.phucx.phucxfoodshop.config.WebSocketConfig;
import com.phucx.phucxfoodshop.model.NotificationDetail;
import com.phucx.phucxfoodshop.service.notification.NotificationService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService{
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate; 
    @Autowired
    private UserService userService;

    @Override
    public void sendMessageToUser(String userID, NotificationDetail notification) {
        log.info("sendMessageToUser(userID={}, notification={})", userID, notification.toString());
        // save notification
        notificationService.createNotification(notification);
        String username = userService.getUserById(userID).getUsername();
        // send notification
        simpMessagingTemplate.convertAndSendToUser(username, WebSocketConfig.QUEUE_MESSAGES, notification, getHeaders());
    }
    
    private Map<String, Object> getHeaders(){
        Map<String, Object> header = new HashMap<>();
        header.put("auto-delete", "true");
        header.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
        return header;
    }

    @Override
    public void sendNotificationToTopic(NotificationDetail notification, String topic) {
        log.info("sendNotificationToTopic(notification={}, topic={})", notification, topic);
        notificationService.createNotification(notification);
        // send notification to notification/order topic
        this.simpMessagingTemplate.convertAndSend(topic, notification);
    }

    // @Override
    // public void sendOrder(OrderWithProducts order) throws JsonProcessingException {
    //     log.info("sendOrder({})", order);
    //     String message = objectMapper.writeValueAsString(order);
    //     // send order to message queue
    //     this.rabbitTemplate.convertAndSend(MessageQueueConfig.ORDER_EXCHANGE, MessageQueueConfig.ORDER_PROCESSING_ROUTING_KEY, message);
    // }

}
