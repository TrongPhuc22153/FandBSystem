package com.phucx.phucxfandb.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCustomerDTO {
    @Size(min = 1, max = 36, message = "Customer ID must be between 1 and 36 characters")
    private String customerId;

    @NotBlank(message = "Contact name cannot be blank")
    @Size(min = 2, max = 100, message = "Contact name must be between 2 and 100 characters")
    private String contactName;

    @Valid
    @NotNull(message = "profile can not be null")
    private RequestUserProfileDTO profile;
}
