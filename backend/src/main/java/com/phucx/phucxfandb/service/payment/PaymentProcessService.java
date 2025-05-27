package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface PaymentProcessService {
    PaymentProcessingDTO processPayment(Authentication authentication, String paymentId, RequestPaymentDTO requestPaymentDTO) throws IOException;
}
