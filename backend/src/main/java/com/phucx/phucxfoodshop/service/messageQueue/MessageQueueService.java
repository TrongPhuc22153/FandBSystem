package com.phucx.phucxfoodshop.service.messageQueue;

import com.phucx.phucxfoodshop.model.NotificationDetail;

public interface MessageQueueService {
    // send message to user
    public void sendMessageToUser(String userID, NotificationDetail notification);
    // send message to employee's topic
    public void sendNotificationToTopic(NotificationDetail notification, String topic);    

    // send order to order processing queue 
    // public void sendOrder(OrderWithProducts order) throws JsonProcessingException;

}
