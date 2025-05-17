package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RequestShippingAddressDTO {
    private Long id;

    @NotBlank(message = "Ship name cannot be blank")
    @Size(max = 40, message = "Ship name must not exceed 40 characters")
    private String shipName;

    @NotBlank(message = "Ship address cannot be blank")
    @Size(max = 100, message = "Ship address must not exceed 100 characters")
    private String shipAddress;

    @NotBlank(message = "Ship city cannot be blank")
    @Size(max = 50, message = "Ship city must not exceed 50 characters")
    private String shipCity;

    @NotBlank(message = "Ship district cannot be blank")
    @Size(max = 50, message = "Ship district must not exceed 50 characters")
    private String shipDistrict;

    @Size(max = 36, message = "Customer id must not exceed 36 characters")
    private String customerId;

    @NotBlank(message = "Ship ward cannot be blank")
    @Size(max = 50, message = "Ship ward must not exceed 50 characters")
    private String shipWard;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits, optionally starting with a '+'")
    private String phone;

    private Boolean isDefault = false;
}
