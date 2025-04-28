package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestPaymentDTO {
    @Size(min = 1, max = 36, message = "Payment ID must be between 1 and 36 characters")
    private String paymentId;

    @NotNull(message = "Payment date is required")
    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDateTime paymentDate;

    @Size(min = 1, max = 100, message = "Transaction ID must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Transaction ID must be alphanumeric with hyphens")
    private String transactionID;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have up to 10 integer digits and 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @NotBlank(message = "Customer ID cannot be blank")
    @Size(min = 1, max = 50, message = "Customer ID must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Customer ID must be alphanumeric with hyphens")
    private String customerId;

    @NotBlank(message = "Payment method ID cannot be blank")
    @Size(min = 1, max = 50, message = "Payment method ID must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Payment method ID must be alphanumeric with hyphens")
    private String paymentMethodId;
}
