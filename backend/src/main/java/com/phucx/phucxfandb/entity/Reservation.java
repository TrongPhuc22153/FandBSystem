package com.phucx.phucxfandb.entity;

import com.phucx.phucxfandb.constant.ReservationStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
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
    private ReservationTable table;

    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests = 1;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;
}
