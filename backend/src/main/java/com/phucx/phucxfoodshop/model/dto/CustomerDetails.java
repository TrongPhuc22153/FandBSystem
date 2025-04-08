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
@NoArgsConstructor
@SuperBuilder
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
