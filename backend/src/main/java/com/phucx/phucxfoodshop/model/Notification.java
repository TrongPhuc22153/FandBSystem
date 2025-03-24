package com.phucx.phucxfoodshop.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@Table(name = "notifications")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "Notification.CreateNotification", 
        procedureName = "CreateNotification",
        parameters = {
            @StoredProcedureParameter(name="notificationID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="title", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="message", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="senderID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="receiverID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="topicName", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="repliedTo", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="isRead", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="time", mode = ParameterMode.IN, type = LocalDateTime.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        })
})
public class Notification {
    @Id
    private String notificationID;
    private String title;
    private String message;
    private String senderID;
    private String receiverID;
    private String picture;

    private Integer topicID;

    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    public Notification(String title, String message, String senderID, String receiverID, Integer topicID, String status) {
        this();
        this.title = title;
        this.message = message;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.topicID = topicID;
        this.status = status;
    }

    public Notification() {
        this.time = LocalDateTime.now();
    }

}
