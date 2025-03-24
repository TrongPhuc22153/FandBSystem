package com.phucx.phucxfoodshop.model;


import com.phucx.phucxfoodshop.constant.NotificationStatus;
import com.phucx.phucxfoodshop.constant.NotificationTitle;
import com.phucx.phucxfoodshop.constant.NotificationTopic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderNotificationDTO extends NotificationDTO{
    private String orderID;
    
    public OrderNotificationDTO(String orderID, NotificationTitle title, String senderID, String receiverID,
            NotificationTopic topic, NotificationStatus status) {
        super(senderID, receiverID, title, topic, status);
        this.orderID = orderID;
    }

    public OrderNotificationDTO(String orderID, NotificationTitle title, String receiverID,
            NotificationTopic topic, NotificationStatus status) {
        super(receiverID, title, topic, status);
        this.orderID = orderID;
    }
}
