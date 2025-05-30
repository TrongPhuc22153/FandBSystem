package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.enums.ReceiverType;
import com.phucx.phucxfandb.enums.SenderType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_users")
@EqualsAndHashCode(callSuper = true)
public class NotificationUser extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "notification_id", referencedColumnName = "notification_id", nullable = false)
    private Notification notification;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private ReceiverType receiverType;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "receiver_role_id", referencedColumnName = "role_id")
    private Role receiverRole;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
}
