package com.phucx.phucxfoodshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "User.CreateCustomerUser", procedureName = "CreateCustomerUser",
    parameters = {
        @StoredProcedureParameter(name="userID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="customerID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="profileID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="firstname", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="lastname", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="email", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="username", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="password", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "User.CreateEmployeeUser", procedureName = "CreateEmployeeUser",
    parameters = {
        @StoredProcedureParameter(name="userID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="employeeID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="profileID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="firstname", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="lastname", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="email", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="username", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="password", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "User.UpdateEmailVerification", procedureName = "UpdateEmailVerification",
    parameters = {
        @StoredProcedureParameter(name="username", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="status", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    }),
    @NamedStoredProcedureQuery(name = "User.UpdateUserPassword", procedureName = "UpdateUserPassword",
    parameters = {
        @StoredProcedureParameter(name="userID", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="password", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="result", type = Boolean.class, mode = ParameterMode.OUT),
    })
})
public class User{
    @Id
    private String userID;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
}
