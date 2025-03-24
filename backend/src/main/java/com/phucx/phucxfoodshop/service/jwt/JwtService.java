package com.phucx.phucxfoodshop.service.jwt;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String createToken(String id, Map<String, Object> claims, String subject, Date issuedAt, Date expiryDate);
    // extract claims
    Claims extractAllClaims(String token);
    String extractUsername(String token);
    Date extractExpiration(String token);
    Boolean isTokenExpired(String token);
}
