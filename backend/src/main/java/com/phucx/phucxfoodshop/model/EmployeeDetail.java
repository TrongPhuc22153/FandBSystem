package com.phucx.phucxfoodshop.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employeedetails")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "employeedetails.UpdateAdminEmployeeInfo",
        procedureName = "UpdateAdminEmployeeInfo", parameters = {
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="hireDate", mode = ParameterMode.IN, type = LocalDate.class),
            @StoredProcedureParameter(name="birthDate", mode = ParameterMode.IN, type = LocalDate.class),
            @StoredProcedureParameter(name="address", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="city", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="district", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="ward", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="title", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="notes", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="enabled", mode = ParameterMode.IN, type = Boolean.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(name = "employeedetails.UpdateEmployeeInfo",
        procedureName = "UpdateEmployeeInfo", parameters = {
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="birthDate", mode = ParameterMode.IN, type = LocalDate.class),
            @StoredProcedureParameter(name="address", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="city", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="district", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="ward", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="phone", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="picture", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type = Boolean.class),
        }),
    @NamedStoredProcedureQuery(name = "employeeaccount.AddNewEmployee",
        procedureName = "AddNewEmployee", parameters = {
            @StoredProcedureParameter(name="profileID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="userID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="employeeID", mode = ParameterMode.IN, type=String.class),
            @StoredProcedureParameter(name="result", mode = ParameterMode.OUT, type=Boolean.class)
        })
})
public class EmployeeDetail implements Serializable{
    @Id
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
    public EmployeeDetail(String userID) {
        this.userID = userID;
    }
    public EmployeeDetail(String employeeID, String userID) {
        this.employeeID = employeeID;
        this.userID = userID;
    }
}
