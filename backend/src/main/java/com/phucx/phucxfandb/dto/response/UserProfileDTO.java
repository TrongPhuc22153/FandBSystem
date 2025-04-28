package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserProfileDTO {
    String address;
    String ward;
    String district;
    String city;
    String phone;
    String picture;

    UserDTO user;
}
