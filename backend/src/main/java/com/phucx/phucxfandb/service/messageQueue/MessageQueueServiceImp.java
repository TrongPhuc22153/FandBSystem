package com.phucx.phucxfandb.service.messageQueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageQueueServiceImp implements MessageQueueService{
//    @Autowired
//    private NotificationService notificationService;
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public void sendMessageToUser(String userID, NotificationDetail notification) {
//        log.info("sendMessageToUser(userID={}, notification={})", userID, notification.toString());
//        // save notification
//        notificationService.createOrderNotification(notification);
//        String username = userService.getUserById(userID).getUsername();
//        // send notification
//        simpMessagingTemplate.convertAndSendToUser(username, WebSocketConfig.QUEUE_MESSAGES, notification, getHeaders());
//    }
//
//    private Map<String, Object> getHeaders(){
//        Map<String, Object> header = new HashMap<>();
//        header.put("auto-delete", "true");
//        header.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
//        return header;
//    }
//
//    @Override
//    public void sendNotificationToTopic(NotificationDetail notification, String topic) {
//        log.info("sendNotificationToTopic(notification={}, topic={})", notification, topic);
//        notificationService.createOrderNotification(notification);
//        // send notification to notification/order topic
//        this.simpMessagingTemplate.convertAndSend(topic, notification);
//    }

    // @Override
    // public void sendOrder(OrderWithProducts order) throws JsonProcessingException {
    //     log.info("sendOrder({})", order);
    //     String message = objectMapper.writeValueAsString(order);
    //     // send order to message queue
    //     this.rabbitTemplate.convertAndSend(MessageQueueConfig.ORDER_EXCHANGE, MessageQueueConfig.ORDER_PROCESSING_ROUTING_KEY, message);
    // }

}
