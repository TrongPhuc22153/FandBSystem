package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order cannot be null")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    @NotNull(message = "Reservation cannot be null")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @Column(nullable = false)
    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(length = 500)
    private String comment;

}
