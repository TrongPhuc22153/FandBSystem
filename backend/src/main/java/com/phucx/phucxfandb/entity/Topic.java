package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "topics")
@EqualsAndHashCode(callSuper = false)
public class Topic extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id", nullable = false, updatable = false)
    private Long topicId;

    @Column(name = "topic_name", length = 20, nullable = false)
    private String topicName;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
