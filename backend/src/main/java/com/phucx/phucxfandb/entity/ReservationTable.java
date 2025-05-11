package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.constant.TableStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tables")
@EqualsAndHashCode(callSuper = true)
public class ReservationTable extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "table_id", nullable = false, length = 36, updatable = false)
    private String tableId;

    @Min(1)
    @Column(name = "table_number", unique = true, nullable = false)
    private Integer tableNumber = 1;

    @NotBlank
    @Column(name = "location", length = 100, nullable = false)
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private TableStatus status;

    @Min(1)
    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity = 1;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
