package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Rating extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @Column(name = "score", nullable = false, precision = 2, scale = 1)
    @NotNull(message = "Score cannot be null")
    @DecimalMin(value = "1.0", message = "Score must be at least 1")
    @DecimalMax(value = "5.0", message = "Score cannot exceed 5")
    private BigDecimal score;

    @Column(name = "comment", length = 200)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", updatable = false, nullable = false)
    private Product product;

}
