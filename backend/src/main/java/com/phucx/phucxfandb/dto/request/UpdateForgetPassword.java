package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateForgetPassword {
    @NotBlank(message = "User ID cannot be empty")
    @Size(min = 3, max = 100, message = "User ID must be between 3 and 100 characters")
    private String userId;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number")
    private String password;
}
