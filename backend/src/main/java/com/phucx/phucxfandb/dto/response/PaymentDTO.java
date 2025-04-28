package com.phucx.phucxfandb.dto.response;

import com.phucx.phucxfandb.constant.PaymentStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class PaymentDTO {
    String paymentId;
    LocalDateTime paymentDate;
    String transactionID;
    BigDecimal amount;
    PaymentStatus status;
    CustomerDTO customer;
    PaymentMethodDTO method;
}
