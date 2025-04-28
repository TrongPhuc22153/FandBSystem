package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PaymentMethodDTO {
    String methodId;
    String methodName;
    String details;
}
