package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.enums.PaymentStatus;

public interface PaymentUpdateService {
    void updatePaypalPaymentStatus(String id, PaymentStatus status);

    void updatePayPalOrder(String paymentId, String paypalOrderId, PaymentStatus status);

    void updatePayment(String paymentId, String methodName, PaymentStatus status);
}
