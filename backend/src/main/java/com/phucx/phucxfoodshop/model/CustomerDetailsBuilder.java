package com.phucx.phucxfoodshop.model;

public class CustomerDetailsBuilder {
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

        public CustomerDetailsBuilder withCustomerID(String customerID) {
            this.customerID = customerID;
            return this;
        }

        public CustomerDetailsBuilder withUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public CustomerDetailsBuilder withContactName(String contactName) {
            this.contactName = contactName;
            return this;
        }

        public CustomerDetailsBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public CustomerDetailsBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public CustomerDetailsBuilder withDistrict(String district) {
            this.district = district;
            return this;
        }

        public CustomerDetailsBuilder withWard(String ward) {
            this.ward = ward;
            return this;
        }

        public CustomerDetailsBuilder withPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public CustomerDetailsBuilder withPicture(String picture) {
            this.picture = picture;
            return this;
        }

        public CustomerDetailsBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public CustomerDetailsBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerDetailsBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerDetailsBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        // Build method to create a new CustomerDetails instance
        public CustomerDetails build() {
            CustomerDetails customerDetails = new CustomerDetails();
            customerDetails.setCustomerID(customerID);
            customerDetails.setUserID(userID);
            customerDetails.setContactName(contactName);
            customerDetails.setAddress(address);
            customerDetails.setCity(city);
            customerDetails.setDistrict(district);
            customerDetails.setWard(ward);
            customerDetails.setPhone(phone);
            customerDetails.setPicture(picture);
            customerDetails.setUsername(username);
            customerDetails.setFirstName(firstName);
            customerDetails.setLastName(lastName);
            customerDetails.setEmail(email);
            return customerDetails;
        }
    }
