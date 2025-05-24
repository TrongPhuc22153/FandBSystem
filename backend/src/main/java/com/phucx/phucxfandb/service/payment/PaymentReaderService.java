package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.entity.Payment;

public interface PaymentReaderService {
    Payment getPaymentEntity(String paymentId);
    Payment getPaymentEntityByPaypalOrderId(String orderId);
}
