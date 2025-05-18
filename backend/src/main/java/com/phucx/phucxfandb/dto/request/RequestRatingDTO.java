package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RequestRatingDTO {
    private String userId;

    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be a positive value")
    private Long productId;

    @Size(max = 200, message = "Comment cannot exceed 200 characters")
    private String comment;

    @NotNull(message = "Score cannot be null")
    @DecimalMin(value = "1.0", message = "Score must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Score must be at most 5.0")
    private BigDecimal score;
}
