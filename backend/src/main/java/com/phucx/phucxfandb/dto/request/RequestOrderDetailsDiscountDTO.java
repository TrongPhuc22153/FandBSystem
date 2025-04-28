package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDetailsDiscountDTO {
    @NotBlank(message = "Discount ID cannot be empty")
    @Size(min = 3, max = 36, message = "Discount ID must be between 3 and 36 characters")
    private String discountId;

    @NotNull(message = "Discount percent cannot be null")
    @PositiveOrZero(message = "Discount percent must be zero or positive")
    @Size(max = 100, message = "Discount percent must not exceed 100")
    private Integer discountPercent;

    @NotNull(message = "Applied date cannot be null")
    @PastOrPresent(message = "Applied date must be in the past or present")
    private LocalDateTime appliedDate;
}
