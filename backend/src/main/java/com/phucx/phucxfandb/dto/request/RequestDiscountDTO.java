package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDiscountDTO {

    private String discountId;

    @NotNull(message = "Discount percent cannot be null")
    @Min(value = 1, message = "Discount percent must be at least 1")
    @Max(value = 100, message = "Discount percent must not exceed 100")
    private Integer discountPercent;

    @NotNull(message = "Discount type ID cannot be null")
    @Min(value = 1, message = "Discount type ID must be a positive integer")
    private Long discountTypeId;

    @NotNull(message = "Product id cannot be null")
    @Min(value = 1, message = "Product id must be a positive integer")
    private Long productId;

    @Size(min = 3, max = 50, message = "Discount code must be between 3 and 50 characters")
    @Pattern(regexp = "[A-Z0-9-]+", message = "Discount code can only contain uppercase letters, numbers, and hyphens")
    private String discountCode;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDateTime endDate;

    private Boolean active = false;
}
