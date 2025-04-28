package com.phucx.phucxfandb.service.payment;

import com.phucx.phucxfandb.constant.PaymentStatus;
import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentDTO;

public interface PaymentUpdateService {

    PaymentDTO createPayment(RequestPaymentDTO requestPaymentDTO);
    PaymentDTO updateOrderPayment(String orderID, PaymentStatus status);
    PaymentDTO updateReservationPayment(String reservationId, PaymentStatus status);
}
