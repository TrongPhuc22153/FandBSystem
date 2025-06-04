package com.phucx.phucxfandb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.phucx.phucxfandb.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@EqualsAndHashCode(callSuper = false)
public class Payment extends Auditable{
    @Id
    @Column(name = "payment_id", nullable = false, length = 36, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Builder.Default
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "paypal_order_id", length = 36)
    private String paypalOrderId;

    @Column(name = "paypal_capture_id", length = 36)
    private String paypalCaptureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "method_id")
    private PaymentMethod method;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Order order;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Reservation reservation;

    @Version
    private Long version;

}
