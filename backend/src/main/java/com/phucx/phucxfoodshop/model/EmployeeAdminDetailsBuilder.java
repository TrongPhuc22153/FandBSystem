package com.phucx.phucxfoodshop.model;

import java.time.LocalDate;

public class EmployeeAdminDetailsBuilder {
    private String employeeID;
    private String userID;
    private LocalDate birthDate;
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

    private Boolean emailVerified;
    private Boolean enabled;

    public EmployeeAdminDetailsBuilder withEmployeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public EmployeeAdminDetailsBuilder withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public EmployeeAdminDetailsBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public EmployeeAdminDetailsBuilder withHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public EmployeeAdminDetailsBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public EmployeeAdminDetailsBuilder withPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public EmployeeAdminDetailsBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public EmployeeAdminDetailsBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public EmployeeAdminDetailsBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public EmployeeAdminDetailsBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public EmployeeAdminDetailsBuilder withWard(String ward) {
        this.ward = ward;
        return this;
    }

    public EmployeeAdminDetailsBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public EmployeeAdminDetailsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public EmployeeAdminDetailsBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeAdminDetailsBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeAdminDetailsBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public EmployeeAdminDetailsBuilder withEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public EmployeeAdminDetailsBuilder withEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public EmployeeAdminDetails build() {
        EmployeeAdminDetails employeeAdminDetails = new EmployeeAdminDetails();
        employeeAdminDetails.setEmployeeID(employeeID);
        employeeAdminDetails.setUserID(userID);
        employeeAdminDetails.setBirthDate(birthDate);
        employeeAdminDetails.setHireDate(hireDate);
        employeeAdminDetails.setPhone(phone);
        employeeAdminDetails.setPicture(picture);
        employeeAdminDetails.setTitle(title);
        employeeAdminDetails.setAddress(address);
        employeeAdminDetails.setCity(city);
        employeeAdminDetails.setDistrict(district);
        employeeAdminDetails.setWard(ward);
        employeeAdminDetails.setNotes(notes);
        employeeAdminDetails.setUsername(username);
        employeeAdminDetails.setFirstName(firstName);
        employeeAdminDetails.setLastName(lastName);
        employeeAdminDetails.setEmail(email);
        employeeAdminDetails.setEmailVerified(emailVerified);
        employeeAdminDetails.setEnabled(enabled);
        return employeeAdminDetails;
    }
}
