package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestCartItemDTO {
    @Size(min = 1, max = 36, message = "Cart ID must be between 1 and 36 characters")
    private String cartId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    @Max(value = 100, message = "Quantity cannot exceed 100")
    private Integer quantity;
}
