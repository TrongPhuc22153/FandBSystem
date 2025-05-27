package com.phucx.phucxfandb.service.payment;

import org.springframework.security.core.Authentication;

public interface CODService {
    void createCODPayment(Authentication authentication, String orderId, String reservationId);
}
