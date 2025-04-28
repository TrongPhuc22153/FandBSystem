package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discount_types")
public class DiscountType extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_type_id", nullable = false, updatable = false)
    private Long discountTypeId;

    @Column(name = "discount_type", nullable = false, length = 10)
    private String discountType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
