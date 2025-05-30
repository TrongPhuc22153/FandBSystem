package com.phucx.phucxfandb.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDTO {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 20, message = "First name must be between 2 and 20 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastName;

    private String password;

    @NotNull(message = "Enabled Status cannot be null",
            groups = ValidationGroups.UpdateUserEnabledStatus.class)
    @JsonView(Views.UpdateUserEnabledStatus.class)
    private Boolean enabled = false;

    @NotEmpty(message = "Roles cannot be empty")
    private List<RoleName> roles;
}
