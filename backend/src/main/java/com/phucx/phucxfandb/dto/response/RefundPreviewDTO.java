package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundPreviewDTO {
    Boolean eligible;
    BigDecimal refundAmount;
    Double refundPercentage;
    String currency;
    String paymentMethod;
    Boolean refundable;
    String reason;
    String status;
    String orderType;
    String paymentStatus;
    String paymentId;
    String refundPolicyNote;
}
