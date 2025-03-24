package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetails {
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
    
}
