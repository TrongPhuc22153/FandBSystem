package com.phucx.phucxfoodshop.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetails {
    private String employeeID;
    private String userID;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    private String phone;
    private String picture;
    private String title;
    
    private String address;
    private String city;
    private String district;
    private String ward;

    private String notes;

    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
