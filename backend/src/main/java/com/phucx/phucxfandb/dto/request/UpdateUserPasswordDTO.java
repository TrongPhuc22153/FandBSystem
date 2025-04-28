package com.phucx.phucxfandb.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPasswordDTO {

    @NotBlank(message = "User id is required")
    private String userId;

    @NotBlank(message = "Old password is required")
    private String password;

    @NotBlank(message = "New password is required")
    private String newPassword;
    @Email

    @NotBlank(message = "Email is required")
    private String email;
}
