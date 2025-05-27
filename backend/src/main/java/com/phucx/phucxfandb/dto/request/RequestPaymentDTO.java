package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestPaymentDTO {

    @Size(min = 1, max = 100, message = "Transaction ID must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Transaction ID must be alphanumeric with hyphens")
    private String transactionID;

    @NotBlank(message = "Order Id cannot be blank",
            groups = ValidationGroups.OrderPayment.class)
    private String orderId;

    @NotBlank(message = "Reservation Id cannot be blank",
            groups = ValidationGroups.ReservationPayment.class)
    private String reservationId;

    @NotNull(message = "ReturnUrl cannot be null")
    private String returnUrl;

    @NotNull(message = "CancelUrl cannot be null")
    private String cancelUrl;

    @Size(min = 1, max = 36, message = "Customer Id must be between 1 and 36 characters")
    private String customerId;

    @NotBlank(message = "Payment method cannot be blank")
    @Size(min = 1, max = 20, message = "Payment method must be between 1 and 20 characters")
    private String paymentMethod;
}
