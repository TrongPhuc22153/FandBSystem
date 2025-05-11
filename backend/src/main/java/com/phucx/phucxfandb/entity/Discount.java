package com.phucx.phucxfandb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discounts")
@EqualsAndHashCode(callSuper = true)
public class Discount extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "discount_id", nullable = false, length = 36, updatable = false)
    private String discountId;

    @Min(0)
    @Max(100)
    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;

    @ManyToOne
    @JoinColumn(name = "discount_type_id", nullable = false)
    private DiscountType discountType;

    @NotBlank
    @Column(name = "discount_code", unique = true, length = 20, nullable = false)
    private String discountCode;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToMany
    @JoinTable(
        name = "products_discounts",
            joinColumns = {@JoinColumn(name = "discount_id", referencedColumnName = "discount_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "product_id")}
    )
    private List<Product> products = new ArrayList<>();
}
