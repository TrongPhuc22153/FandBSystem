package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestFeedBackDTO {
    @Positive(message = "ID must be positive if provided")
    private Long id;

    @Size(max = 36, message = "Order Id must be around 36 characters")
    private String orderId;

    @Size(max = 36, message = "Reservation Id must be around 36 characters")
    private String reservationId;

    @NotBlank(message = "Customer ID cannot be blank")
    @Size(max = 36, message = "Customer Id must be around 36 characters")
    private String customerId;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;
}
