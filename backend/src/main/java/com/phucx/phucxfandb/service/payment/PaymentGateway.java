package com.phucx.phucxfandb.service.payment;

import java.math.BigDecimal;

public interface PaymentGateway {
    boolean processPayment(BigDecimal amount, String methodId, String customerId, String orderId);
}
