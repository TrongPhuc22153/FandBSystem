package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder
public class UserDTO {
    String userId;
    String username;
    String email;
    String firstName;
    String lastName;
    Boolean enabled;
    Boolean emailVerified;
    Set<RoleDTO> roles;
    LocalDateTime createdAt;

}
