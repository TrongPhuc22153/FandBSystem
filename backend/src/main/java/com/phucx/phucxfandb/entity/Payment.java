package com.phucx.phucxfandb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.phucx.phucxfandb.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@EqualsAndHashCode(callSuper = true)
public class Payment extends Auditable{
    @Id
    @Column(name = "payment_id", nullable = false, length = 36, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "transaction_id", length = 100)
    private String transactionID;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private PaymentStatus status;

//    @OneToOne(mappedBy = "payment")
//    private Order order;

//    @OneToOne(mappedBy = "payment")
//    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "method_id", nullable = false)
    private PaymentMethod method;

}
