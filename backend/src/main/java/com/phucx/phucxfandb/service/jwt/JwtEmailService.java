package com.phucx.phucxfandb.service.jwt;

import com.phucx.phucxfandb.constant.JwtType;

public interface JwtEmailService {

    String extractEmail(String token);

    String generateToken(String email, JwtType type);

    boolean validateToken(String token, JwtType type);
}
