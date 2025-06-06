package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductDTO {
    @Positive(message = "Product ID must be a positive number")
    private Integer productId;

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 1, max = 40, message = "Product name must be between 1 and 40 characters")
    private String productName;

    @NotNull(message = "Category id cannot be null")
    private Integer categoryId;

    @NotNull(message = "Unit price cannot be null")
    @Positive(message = "Unit price must be a positive number")
    private BigDecimal unitPrice;

    @NotNull(message = "Product units in stock cannot be null", groups = ValidationGroups.UpdateProductQuantity.class)
    @Min(value = 0, message = "Units in stock cannot be negative")
    private Integer unitsInStock;

    @Min(value = 0, message = "Minimum stock cannot be negative")
    private Integer minimumStock;

    @NotNull(message = "Is featured cannot be null")
    private Boolean isFeatured = false;

    @Size(max = 255, message = "Picture URL/path must not exceed 255 characters")
    private String picture;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Boolean isDeleted = false;
}
