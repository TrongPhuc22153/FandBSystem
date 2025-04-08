package com.phucx.phucxfoodshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAdminDetails extends EmployeeDetails {
    private Boolean emailVerified;
    private Boolean enabled;
}
