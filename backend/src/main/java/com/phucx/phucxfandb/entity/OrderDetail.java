package com.phucx.phucxfandb.entity;

import java.math.BigDecimal;


import com.phucx.phucxfandb.enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_details")
@EqualsAndHashCode(callSuper = false)
public class OrderDetail extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @EqualsAndHashCode.Exclude
    private Order order;

    @Column(name = "special_instruction")
    private String specialInstruction;

    @Builder.Default
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderItemStatus status = OrderItemStatus.PENDING;

    @Builder.Default
    @Column(name = "quantity")
    private Integer quantity = 1;
}
