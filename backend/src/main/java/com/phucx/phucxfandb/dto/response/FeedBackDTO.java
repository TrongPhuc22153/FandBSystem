package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedBackDTO {
    @NotNull(message = "Order cannot be null")
    OrderDTO order;

    @NotNull(message = "Customer cannot be null")
    CustomerDTO customer;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    Integer rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    String comment;

    LocalDateTime createdAt;
}