package com.phucx.phucxfandb.service.jwt;


import com.phucx.phucxfandb.entity.User;
public interface JwtAuthenticationService {

    String generateAuthToken(User user);
    // extract claims
    String extractUsername(String token);
    Boolean validateToken(String token);
}
