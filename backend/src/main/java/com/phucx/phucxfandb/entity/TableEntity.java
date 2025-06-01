package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables")
@EqualsAndHashCode(callSuper = false)
public class TableEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "table_id", nullable = false, length = 36, updatable = false)
    private String tableId;

    @Column(name = "table_number", unique = true, nullable = false)
    private Integer tableNumber;

    @Column(name = "location", length = 100, nullable = false)
    private String location;

    @Builder.Default
    @Column(name = "capacity", nullable = false)
    private Integer capacity = 1;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
