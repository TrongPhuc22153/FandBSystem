package com.phucx.phucxfandb.dto.response;

import lombok.*;

@Value
@Builder
public class PaymentResponse {
    Boolean status;
    String message;
    String redirect_url;
}
