package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUpdateServiceImpl implements PaymentUpdateService {
    private final PaymentMethodReaderService paymentMethodReaderService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void updatePaypalPaymentStatus(String paypalOrderId, PaymentStatus status) {
        Payment payment = paymentRepository.findByPaypalOrderId(paypalOrderId)
                .orElseThrow(() -> new NotFoundException(
                        Payment.class.getSimpleName(),
                        "paypal order id",
                        paypalOrderId));

        payment.setStatus(status);
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void updatePayPalOrder(String paymentId, String paypalOrderId, PaymentStatus status) {
        PaymentMethod method = paymentMethodReaderService
                .getPaymentMethodEntityByName(PaymentMethodConstants.PAY_PAL);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(
                        Payment.class.getSimpleName(),
                        "id",
                        paymentId));
        payment.setMethod(method);
        payment.setStatus(status);
        payment.setPaypalOrderId(paypalOrderId);
        paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void updatePayment(String paymentId, String methodName, PaymentStatus status) {
        PaymentMethod method = paymentMethodReaderService.getPaymentMethodEntityByName(methodName);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(
                        Payment.class.getSimpleName(),
                        "id",
                        paymentId));
        payment.setMethod(method);
        payment.setStatus(status);
        paymentRepository.save(payment);
    }
}
