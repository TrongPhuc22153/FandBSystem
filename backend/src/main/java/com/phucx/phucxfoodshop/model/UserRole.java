package com.phucx.phucxfoodshop.model;

import com.phucx.phucxfoodshop.compositeKey.UserRoleID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserRoleID.class)
@Table(name = "userroles")
public class UserRole {
    @Id
    private String userID;
    private String username;
    private String email;
    @Id
    private Integer roleID;
    private String roleName;
}
