package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductSizeDTO {
    private String id;

    @NotNull(message = "Height cannot be null")
    @Positive(message = "Height must be a positive number")
    private Integer height;

    @NotNull(message = "Length cannot be null")
    @Positive(message = "Length must be a positive number")
    private Integer length;

    @NotNull(message = "Weight cannot be null")
    @Positive(message = "Weight must be a positive number")
    private Integer weight;

    @NotNull(message = "Width cannot be null")
    @Positive(message = "Width must be a positive number")
    private Integer width;

    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be a positive number")
    private Integer productId;
}
