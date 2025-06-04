package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PayPalRefundDTO {
    String id;
    String status;
    MoneyDTO amount;
}
