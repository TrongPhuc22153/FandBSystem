package com.phucx.phucxfandb.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValidateProductDiscountsDTO {
    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;

    @NotEmpty(message = "Discount IDs list cannot be empty")
    @Size(min = 1, message = "At least one discount ID must be provided")
    private List<@NotNull(message = "Discount ID cannot be null") String> discountIds;

    @NotNull(message = "Applied date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appliedDate;
}
