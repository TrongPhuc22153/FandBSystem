package com.phucx.phucxfandb.service.payment;

import com.paypal.orders.Order;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.math.BigDecimal;

public interface PayPalService {
    String createOrder(String paymentId, BigDecimal amount, String currency, String returnUrl, String cancelUrl) throws IOException;
    Order captureOrder(String paypalOrderId) throws IOException;
    void completeOrder(Authentication authentication, String paypalOrderId);
}
