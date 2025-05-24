package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentOrderDTO {
    @NotNull(message = "Payment Id cannot be null")
    private String paymentId;

    @Builder.Default
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.00", message = "Amount cannot be bellow 0.00")
    private BigDecimal amount = BigDecimal.ZERO;

    @NotNull(message = "Currency cannot be null")
    private String currency;

    @NotNull(message = "ReturnUrl cannot be null")
    private String returnUrl;

    @NotNull(message = "CancelUrl cannot be null")
    private String cancelUrl;
}
