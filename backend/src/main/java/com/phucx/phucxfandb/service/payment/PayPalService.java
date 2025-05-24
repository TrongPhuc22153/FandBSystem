package com.phucx.phucxfandb.service.payment;

import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.math.BigDecimal;

public interface PayPalService {
    String createOrder(String paymentId, BigDecimal amount, String currency, String returnUrl, String cancelUrl) throws IOException;
    String captureOrder(String orderId);
    void completeOrder(Authentication authentication, String orderId);
}
