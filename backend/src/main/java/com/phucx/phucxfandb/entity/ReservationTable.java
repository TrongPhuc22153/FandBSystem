package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables")
@EqualsAndHashCode(callSuper = false)
public class ReservationTable extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "table_id", nullable = false, length = 36, updatable = false)
    private String tableId;

    @Column(name = "table_number", unique = true, nullable = false)
    private Integer tableNumber = 1;

    @Column(name = "location", length = 100, nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private TableStatus status;

    @Column(name = "capacity", nullable = false)
    private Integer capacity = 1;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
