package com.phucx.phucxfoodshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthentication {
    private UserInfo user; 
    private List<String> roles;
}
