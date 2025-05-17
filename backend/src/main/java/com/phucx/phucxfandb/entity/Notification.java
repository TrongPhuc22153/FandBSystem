package com.phucx.phucxfandb.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
@EqualsAndHashCode(callSuper = true)
public class Notification extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id", length = 36, nullable = false)
    private String notificationId;

    @NotBlank
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotBlank
    @Column(name = "message", length = 500, nullable = false)
    private String message;

    @Column(name = "picture", length = 255)
    private String picture;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationUser> notificationUsers = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "notification_time", nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "repliedTo", referencedColumnName = "notification_id")
    private Notification notification;



}
