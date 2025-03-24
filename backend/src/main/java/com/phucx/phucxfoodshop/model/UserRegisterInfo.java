package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterInfo {
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String confirmPassword;
    private String email;
}
