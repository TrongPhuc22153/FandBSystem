package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.dto.request.PaymentRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.enums.PaymentStatus;
import org.springframework.data.domain.Page;

public interface PaymentReaderService {
    Payment getPaymentEntity(String paymentId);
    Payment getPaymentEntityByPaypalOrderId(String orderId);

    Page<PaymentDTO> getPayments(PaymentRequestParamsDTO params);
    PaymentDTO getPayment(String id);

    boolean existsByPayPalOrderIdAndStatus(String paypalOrderId, PaymentStatus status);
}
