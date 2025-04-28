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
public class LoginUserDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @Size(min = 5, max = 20, message = "Password must be around 5 to 20 characters")
    @NotBlank(message = "Password is required")
    private String password;
}
