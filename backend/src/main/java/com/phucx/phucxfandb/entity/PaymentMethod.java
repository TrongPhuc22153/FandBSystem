package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment_methods")
@EqualsAndHashCode(callSuper = false)
public class PaymentMethod extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "method_id", nullable = false, length = 36)
    private String methodId;

    @Column(name = "method_name", length = 20, nullable = false)
    private String methodName;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "methods")
    private Set<PaymentMethodType> types = new HashSet<>();

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
}
