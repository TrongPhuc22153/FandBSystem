package com.phucx.phucxfandb.service.jwt;


import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface JwtAuthenticationService {

    String generateAuthToken(User user);

    String extractUsername(String token);

    Set<RoleName> extractRoles(String token);

    UsernamePasswordAuthenticationToken extractAuthentication(String token);

    Boolean validateToken(String token);
}
