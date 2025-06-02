package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
@EqualsAndHashCode(callSuper = false)
public class Reservation extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reservation_id", nullable = false, length = 36, updatable = false)
    private String reservationId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private TableEntity table;

    @Builder.Default
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests = 1;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "table_occupancy_id")
    private TableOccupancy tableOccupancy;

    @Builder.Default
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
