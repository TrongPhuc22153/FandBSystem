package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.enums.OccupancyType;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "table_occupancies")
@EqualsAndHashCode(callSuper = false)
public class TableOccupancy extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "contact_name", length = 30, nullable = false)
    private String contactName;

    @Column(name = "phone", length = 24)
    private String phone;

    @Builder.Default
    @Column(name = "party_size", nullable = false)
    private Integer partySize = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "tableOccupancy", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Order order;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "tableOccupancy", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Reservation reservation;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TableOccupancyStatus status = TableOccupancyStatus.WAITING;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OccupancyType type;

    @Column(name = "notes")
    private String notes;
}
