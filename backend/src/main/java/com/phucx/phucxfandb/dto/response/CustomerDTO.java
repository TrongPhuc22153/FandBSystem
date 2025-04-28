package com.phucx.phucxfandb.dto.response;

import lombok.*;

@Value
@Builder
public class CustomerDTO {
    String customerId;
    String contactName;
    UserProfileDTO profile;
}
