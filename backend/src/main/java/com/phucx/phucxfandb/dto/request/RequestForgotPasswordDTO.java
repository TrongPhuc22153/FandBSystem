package com.phucx.phucxfandb.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestForgotPasswordDTO {
    @NotBlank(message = "Email is required", groups = ValidationGroups.ForgetPassword.class)
    @Email(message = "Invalid email format", groups = ValidationGroups.ForgetPassword.class)
    @JsonView(Views.ForgetPassword.class)
    private String email;

    @NotBlank(message = "Reset token is required", groups = {ValidationGroups.ValidateToken.class, ValidationGroups.ResetPassword.class})
    @JsonView({Views.ValidateToken.class, Views.ResetPassword.class})
    private String token;

    @NotBlank(message = "Password cannot be empty", groups = {ValidationGroups.ResetPassword.class})
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters", groups = ValidationGroups.ResetPassword.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number",
            groups = ValidationGroups.ResetPassword.class)
    @JsonView(Views.ResetPassword.class)
    private String newPassword;
}
