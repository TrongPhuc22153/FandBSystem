package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.constant.Currency;
import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import com.phucx.phucxfandb.entity.PaymentMethod;
import com.phucx.phucxfandb.service.payment.CODService;
import com.phucx.phucxfandb.service.payment.PaymentProcessService;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import com.phucx.phucxfandb.service.payment.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentProcessServiceImpl implements PaymentProcessService {
    private final PaymentMethodReaderService paymentMethodReaderService;
    private final PayPalService payPalService;
    private final CODService codService;

    @Override
    public PaymentProcessingDTO processPayment(Authentication authentication, RequestPaymentDTO requestPaymentDTO) {
        PaymentMethod method = paymentMethodReaderService.getPaymentMethodEntityByName(
                requestPaymentDTO.getPaymentMethod());
        String methodName = method.getMethodName().toLowerCase();

        try {
            return switch (methodName) {
                case PaymentMethodConstants.PAY_PAL -> handlePaypal(authentication, requestPaymentDTO);

                case PaymentMethodConstants.COD -> handleCOD(authentication, requestPaymentDTO);

                default -> throw new IllegalStateException("Unsupported payment method: " + method.getMethodName());
            };
        } catch (IOException ex) {
            throw new RuntimeException("An error occurred during payment processing", ex);
        }
    }

    private PaymentProcessingDTO handlePaypal(Authentication authentication, RequestPaymentDTO requestPaymentDTO) throws IOException {
        String approvedLink = payPalService.createOrder(
                requestPaymentDTO.getPaymentId(),
                requestPaymentDTO.getAmount(),
                Currency.USD.name(),
                requestPaymentDTO.getReturnUrl(),
                requestPaymentDTO.getCancelUrl());

        return  PaymentProcessingDTO.builder()
                .method(PaymentMethodConstants.PAY_PAL)
                .link(approvedLink)
                .build();
    }

    private PaymentProcessingDTO handleCOD(Authentication authentication, RequestPaymentDTO requestPaymentDTO){
        codService.createCODPayment(
                authentication,
                requestPaymentDTO.getPaymentId(),
                requestPaymentDTO.getOrderId(),
                requestPaymentDTO.getReservationId());

        return  PaymentProcessingDTO.builder()
                .method(PaymentMethodConstants.COD)
                .build();
    }
}
