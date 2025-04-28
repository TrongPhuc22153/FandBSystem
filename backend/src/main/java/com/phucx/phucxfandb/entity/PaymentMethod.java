package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_methods")
public class PaymentMethod extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "method_id", nullable = false, length = 36)
    private String methodId;

    @Column(name = "method_name", length = 20, nullable = false)
    private String methodName;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
}
