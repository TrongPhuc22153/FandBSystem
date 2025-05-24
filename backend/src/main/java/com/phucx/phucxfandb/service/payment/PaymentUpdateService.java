package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.constant.PaymentStatus;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.Payment;

import java.math.BigDecimal;

public interface PaymentUpdateService {

    Payment createCustomerPayment(String methodName, BigDecimal amount, String customerId);
    Payment createEmployeePayment(String methodName, BigDecimal amount, String employeeId);

    PaymentDTO updatePaypalPaymentStatus(String id, PaymentStatus status);

    PaymentDTO updatePaypalOrderId(String paymentId, String paypalOrderId);
}
