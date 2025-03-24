package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePassword {
    private String userID;
    private String password;
    private String newPassword;
    private String confirmNewPassword;
    private String email;
}
