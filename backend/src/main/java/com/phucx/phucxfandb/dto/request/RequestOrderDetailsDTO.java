package com.phucx.phucxfandb.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDetailsDTO {
    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @Valid
    private List<RequestOrderDetailsDiscountDTO> orderDetailDiscounts;

    @NotNull(message = "Unit price cannot be null")
    @PositiveOrZero(message = "Unit price must be zero or positive")
    @DecimalMin(value = "0.00")
    private BigDecimal unitPrice;

    @Min(1)
    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
}
