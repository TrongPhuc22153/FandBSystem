package com.phucx.phucxfandb.service.refund;

import com.phucx.phucxfandb.dto.response.PayPalRefundDTO;

public interface PayPalRefundService {
    PayPalRefundDTO refundPayment(String paymentId);
}
