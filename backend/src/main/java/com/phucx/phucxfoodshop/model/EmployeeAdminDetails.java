package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAdminDetails extends EmployeeDetails{
    private Boolean emailVerified;
    private Boolean enabled;
}
