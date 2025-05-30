package com.phucx.phucxfandb.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.phucx.phucxfandb.constant.OrderItemStatus;
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
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailDiscount> orderDetailDiscounts = new ArrayList<>();

    @Builder.Default
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderItemStatus status = OrderItemStatus.PENDING;

    @Builder.Default
    @Column(name = "quantity")
    private Integer quantity = 1;
}
