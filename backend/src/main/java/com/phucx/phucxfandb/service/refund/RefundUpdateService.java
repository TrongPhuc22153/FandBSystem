package com.phucx.phucxfandb.service.refund;

import java.math.BigDecimal;

public interface RefundUpdateService {
    void createRefund(String paymentId, BigDecimal amount);
}
