package com.phucx.phucxfoodshop.model;

import com.phucx.phucxfoodshop.compositeKey.NotificationUserKey;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(NotificationUserKey.class)
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "NotificationUser.UpdateNotificationReadStatusByNotificationIDAndUserID",
        procedureName = "UpdateNotificationReadStatusByNotificationIDAndUserID",
        parameters = {
            @StoredProcedureParameter(name="notificationID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="userID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="isRead", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class)
        }
    ),
    @NamedStoredProcedureQuery(
        name = "NotificationUser.UpdateNotificationReadStatusByUserID",
        procedureName = "UpdateNotificationReadStatusByUserID",
        parameters = {
            @StoredProcedureParameter(name="userID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="isRead", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class)
        }
    ),
    @NamedStoredProcedureQuery(
        name = "NotificationUser.UpdateNotificationReadStatusByNotificationID",
        procedureName = "UpdateNotificationReadStatusByNotificationID",
        parameters = {
            @StoredProcedureParameter(name="notificationID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="isRead", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class)
        }
    ),
    @NamedStoredProcedureQuery(
        name = "NotificationUser.UpdateNotificationsReadByNotificationID",
        procedureName = "UpdateNotificationsReadByNotificationID",
        parameters = {
            @StoredProcedureParameter(name="notificationIDs", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="isRead", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }
    )
})
@Table(name = "notificationuser")
public class NotificationUser {
    @Id
    private String notificationID;
    @Id
    private String userID;
    private Boolean isRead;
}
