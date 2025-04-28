package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDiscountTypeDTO {
    private Long discountTypeId;

    @NotBlank(message = "Discount type cannot be blank")
    @Size(min = 2, max = 10, message = "Discount type must be between 2 and 10 characters")
    private String discountType;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
