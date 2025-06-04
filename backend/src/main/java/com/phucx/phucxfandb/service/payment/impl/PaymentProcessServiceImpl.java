package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.enums.Currency;
import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.service.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentProcessServiceImpl implements PaymentProcessService {
    private final PaymentReaderService paymentReaderService;
    private final PayPalService payPalService;
    private final CashService cashService;
    private final CODService codService;

    @Override
    @Transactional
    public PaymentProcessingDTO processPayment(Authentication authentication, String paymentId, RequestPaymentDTO requestPaymentDTO) throws IOException{
        Payment payment = paymentReaderService.getPaymentEntity(paymentId);
        String methodName = requestPaymentDTO.getPaymentMethod();

        if(methodName.equalsIgnoreCase(PaymentMethodConstants.PAYPAL)) {
            String approvedLink = payPalService.createOrder(
                    paymentId,
                    payment.getAmount(),
                    Currency.USD.name(),
                    requestPaymentDTO.getReturnUrl(),
                    requestPaymentDTO.getCancelUrl());

            return PaymentProcessingDTO.builder()
                    .method(methodName)
                    .link(approvedLink)
                    .build();
        } else if(methodName.equalsIgnoreCase(PaymentMethodConstants.COD)) {
            codService.createCODPayment(
                    authentication,
                    requestPaymentDTO.getOrderId(),
                    requestPaymentDTO.getReservationId());

            return  PaymentProcessingDTO.builder()
                    .method(PaymentMethodConstants.COD)
                    .build();
        } else if (methodName.equalsIgnoreCase(PaymentMethodConstants.CASH)) {
            cashService.createCashPayment(
                    authentication,
                    requestPaymentDTO.getOrderId(),
                    requestPaymentDTO.getReservationId());

            return  PaymentProcessingDTO.builder()
                    .method(PaymentMethodConstants.CASH)
                    .build();
        } else{
            throw new IllegalArgumentException("Unsupported payment method: " + methodName);
        }
    }
}
