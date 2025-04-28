package com.phucx.phucxfandb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMenuItemDTO {
    private String id;

    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be zero or positive")
    private BigDecimal price = BigDecimal.ZERO;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity = 0;
}