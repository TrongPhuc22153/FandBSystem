package com.phucx.phucxfandb.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details_discounts")
@EqualsAndHashCode(callSuper = true)
public class OrderDetailDiscount extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id", nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "discount_id", referencedColumnName = "discount_id", nullable = false)
    private Discount discount;

    @Min(0)
    @Max(100)
    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent = 0;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

}
