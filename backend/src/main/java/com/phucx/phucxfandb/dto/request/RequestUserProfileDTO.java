package com.phucx.phucxfandb.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserProfileDTO {
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @Size(min = 2, max = 50, message = "District must be between 2 and 50 characters")
    private String district;

    @Size(min = 2, max = 50, message = "Ward must be between 2 and 50 characters")
    private String ward;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$",
            message = "Phone number must be valid (e.g., +84901234567 or 0901234567)"
    )
    private String phone;

    @Size(max = 255, message = "Picture URL must not exceed 255 characters")
    private String picture;

    @Valid
    private RequestUserDTO user;

}
