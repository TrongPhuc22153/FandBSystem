package com.phucx.phucxfandb.service.refund.impl;

import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.Refund;
import com.phucx.phucxfandb.repository.RefundRepository;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import com.phucx.phucxfandb.service.refund.RefundUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RefundUpdateServiceImpl implements RefundUpdateService {
    private final RefundRepository refundRepository;
    private final PaymentReaderService paymentReaderService;

    @Override
    @Transactional
    public void createRefund(String paymentId, BigDecimal amount) {
        if(refundRepository.existsByPaymentPaymentId(paymentId)){
            throw new IllegalStateException("Refund already exists for payment ID: " + paymentId);
        }

        Payment payment = paymentReaderService.getPaymentEntity(paymentId);

        if (amount.compareTo(payment.getAmount()) > 0) {
            throw new IllegalArgumentException("Refund amount exceeds original payment amount.");
        }

        Refund refund = Refund.builder()
                .payment(payment)
                .amount(amount)
                .build();
        refundRepository.save(refund);
    }
}
