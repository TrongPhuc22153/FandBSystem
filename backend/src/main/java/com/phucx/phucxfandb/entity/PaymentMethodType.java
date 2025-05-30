package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_method_types")
@EqualsAndHashCode(callSuper = false)
public class PaymentMethodType extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "payment_method_types_payments",
            joinColumns = @JoinColumn(name = "type_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "method_id", referencedColumnName = "method_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<PaymentMethod> methods = new HashSet<>();

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
