package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class EmployeeDTO {
    String employeeId;
    String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate hireDate;
    String notes;
    UserProfileDTO profile;
}
