package com.phucx.phucxfandb.service.payment;

import org.springframework.security.core.Authentication;

public interface CashService {
    void createCashPayment(Authentication authentication, String orderId, String reservationId);
}
