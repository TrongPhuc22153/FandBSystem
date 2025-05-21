package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPaymentOrderDTO {
    @NotNull(message = "Amount cannot be null")
    private Double amount;
    @NotNull(message = "Currency cannot be null")
    private String currency;
    @NotNull(message = "ReturnUrl cannot be null")
    private String returnUrl;
    @NotNull(message = "CancelUrl cannot be null")
    private String cancelUrl;
}
