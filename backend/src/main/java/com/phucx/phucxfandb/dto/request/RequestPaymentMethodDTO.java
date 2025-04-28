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
public class RequestPaymentMethodDTO {
    @Size(min = 1, max = 36, message = "methodId must be between 1 and 36 characters")
    private String methodId;

    @NotBlank(message = "methodName cannot be blank")
    @Size(min = 1, max = 20, message = "methodName must be between 1 and 20 characters")
    private String methodName;

    @Size(max = 500, message = "details cannot exceed 500 characters")
    private String details;

}
