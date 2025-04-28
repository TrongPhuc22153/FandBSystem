package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_users")
public class NotificationUser extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "notification_id", referencedColumnName = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id", nullable = false)
    private User receiver;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
}
