package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data 
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfile{
    private String customerID;
    private String userID;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String contactName;
    private String address;
    private String city;
    private String phone;
    private String picture;
}
