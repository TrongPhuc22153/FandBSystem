package com.phucx.phucxfandb.service.payment.imp;

import com.phucx.phucxfandb.service.payment.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayServiceImpl implements PaymentGateway {
    @Override
    public boolean processPayment(BigDecimal amount, String methodId, String customerId, String orderId) {
        return true;
    }
}
