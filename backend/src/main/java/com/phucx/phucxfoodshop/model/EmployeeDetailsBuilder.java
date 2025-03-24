package com.phucx.phucxfoodshop.model;

import java.time.LocalDate;

public class EmployeeDetailsBuilder {
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

    public EmployeeDetailsBuilder withEmployeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public EmployeeDetailsBuilder withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public EmployeeDetailsBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public EmployeeDetailsBuilder withHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public EmployeeDetailsBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public EmployeeDetailsBuilder withPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public EmployeeDetailsBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public EmployeeDetailsBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public EmployeeDetailsBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public EmployeeDetailsBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public EmployeeDetailsBuilder withWard(String ward) {
        this.ward = ward;
        return this;
    }

    public EmployeeDetailsBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public EmployeeDetailsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public EmployeeDetailsBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public EmployeeDetailsBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public EmployeeDetailsBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public EmployeeDetails build() {
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setEmployeeID(employeeID);
        employeeDetails.setUserID(userID);
        employeeDetails.setBirthDate(birthDate);
        employeeDetails.setHireDate(hireDate);
        employeeDetails.setPhone(phone);
        employeeDetails.setPicture(picture);
        employeeDetails.setTitle(title);
        employeeDetails.setAddress(address);
        employeeDetails.setCity(city);
        employeeDetails.setDistrict(district);
        employeeDetails.setWard(ward);
        employeeDetails.setNotes(notes);
        employeeDetails.setUsername(username);
        employeeDetails.setFirstName(firstName);
        employeeDetails.setLastName(lastName);
        employeeDetails.setEmail(email);
        return employeeDetails;
    }
}