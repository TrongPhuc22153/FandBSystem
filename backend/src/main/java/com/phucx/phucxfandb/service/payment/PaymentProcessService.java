package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import org.springframework.security.core.Authentication;

public interface PaymentProcessService {
    PaymentProcessingDTO processPayment(Authentication authentication, RequestPaymentDTO requestPaymentDTO);
}
