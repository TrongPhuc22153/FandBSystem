package com.phucx.phucxfandb.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestCartDTO {
    @NotBlank(message = "Cart ID cannot be blank")
    @Size(min = 1, max = 36, message = "Cart ID must be between 1 and 36 characters")
    private String id;

    @Valid
    @NotNull(message = "Cart items cannot be null")
    private List<RequestCartItemDTO> cartItems = new ArrayList<>();

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Total price must have up to 10 integer digits and 2 decimal places")
    private BigDecimal totalPrice;
}
