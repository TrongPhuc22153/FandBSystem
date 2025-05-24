package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentReaderServiceImpl implements PaymentReaderService {
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentEntity(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "id", paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentEntityByPaypalOrderId(String orderId) {
        return paymentRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "paypal order id", orderId));
    }
}
