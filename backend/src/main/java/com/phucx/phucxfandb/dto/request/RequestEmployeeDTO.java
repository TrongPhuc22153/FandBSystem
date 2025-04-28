package com.phucx.phucxfandb.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestEmployeeDTO {
    @Size(min = 3, max = 36, message = "Employee ID must be between 3 and 36 characters")
    private String employeeId;

    private String title;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @PastOrPresent(message = "Hire date must be in the past or present")
    private LocalDate hireDate;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    @Valid
    @NotNull(message = "Profile cannot be null")
    private RequestUserProfileDTO profile;
}
