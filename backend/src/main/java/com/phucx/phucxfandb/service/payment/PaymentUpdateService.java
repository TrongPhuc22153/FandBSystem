package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.enums.PaymentStatus;

public interface PaymentUpdateService {
    void updatePaypalPaymentStatus(String paypalOrderId, PaymentStatus status);

    void updatePayPalOrder(String paymentId, String paypalOrderId, PaymentStatus status);

    void updatePayPalPayment(String paypalOrderId, String paypalCaptureId, PaymentStatus status);

    void updatePayment(String paymentId, String methodName, PaymentStatus status);
}
