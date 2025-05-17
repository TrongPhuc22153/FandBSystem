package com.phucx.phucxfandb.service.jwt;


import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.User;

import java.util.Set;

public interface JwtAuthenticationService {

    String generateAuthToken(User user);
    // extract claims
    String extractUsername(String token);

    Set<RoleName> extractRoles(String token);

    Boolean validateToken(String token);
}
