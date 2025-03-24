package com.phucx.phucxfoodshop.model;

public class CustomerAdminDetailsBuilder {
    private String customerID;
    private String userID;
    private String contactName;

    private String address;
    private String city;
    private String district;
    private String ward;

    private String phone;
    private String picture;

    private String username;
    private String firstName;
    private String lastName;
    private String email;

    private Boolean phoneVerified;
    private Boolean profileVerified;
    private Boolean emailVerified;
    private Boolean enabled;

    public CustomerAdminDetailsBuilder withCustomerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    public CustomerAdminDetailsBuilder withUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public CustomerAdminDetailsBuilder withContactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public CustomerAdminDetailsBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerAdminDetailsBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public CustomerAdminDetailsBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public CustomerAdminDetailsBuilder withWard(String ward) {
        this.ward = ward;
        return this;
    }

    public CustomerAdminDetailsBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CustomerAdminDetailsBuilder withPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public CustomerAdminDetailsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public CustomerAdminDetailsBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerAdminDetailsBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerAdminDetailsBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerAdminDetailsBuilder withPhoneVerified(Boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
        return this;
    }

    public CustomerAdminDetailsBuilder withProfileVerified(Boolean profileVerified) {
        this.profileVerified = profileVerified;
        return this;
    }

    public CustomerAdminDetailsBuilder withEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public CustomerAdminDetailsBuilder withEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public CustomerAdminDetails build() {
        CustomerAdminDetails customerAdminDetails = new CustomerAdminDetails();
        customerAdminDetails.setCustomerID(customerID);
        customerAdminDetails.setUserID(userID);
        customerAdminDetails.setContactName(contactName);
        customerAdminDetails.setAddress(address);
        customerAdminDetails.setCity(city);
        customerAdminDetails.setDistrict(district);
        customerAdminDetails.setWard(ward);
        customerAdminDetails.setPhone(phone);
        customerAdminDetails.setPicture(picture);
        customerAdminDetails.setUsername(username);
        customerAdminDetails.setFirstName(firstName);
        customerAdminDetails.setLastName(lastName);
        customerAdminDetails.setEmail(email);
        customerAdminDetails.setPhoneVerified(phoneVerified);
        customerAdminDetails.setProfileVerified(profileVerified);
        customerAdminDetails.setEmailVerified(emailVerified);
        customerAdminDetails.setEnabled(enabled);
        return customerAdminDetails;
    }
}