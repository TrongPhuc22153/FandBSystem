package com.phucx.phucxfoodshop.model;

import com.phucx.phucxfoodshop.constant.ReservationStatus;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    private String reservationID;
    private Integer numberOfGuests;
    private LocalDateTime time;
    private String note;
    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;
    private String customerID;
    private String tableID;
    private String paymentID;
}
