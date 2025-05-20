package com.phucx.phucxfandb.service.jwt;

import com.phucx.phucxfandb.constant.JwtType;

public interface JwtEmailService {

    String extractEmail(String token);

    String generateToken(String email, JwtType type);

    void validateToken(String token, JwtType type);
}
