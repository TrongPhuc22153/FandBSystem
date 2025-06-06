package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    String username;
    String email;
    String accessToken;
    String refreshToken;
}
