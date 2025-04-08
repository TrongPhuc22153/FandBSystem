package com.phucx.phucxfoodshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
public class MessageSender {
    // both of them are userID
    protected String senderID;
    protected String recipientID;
}
