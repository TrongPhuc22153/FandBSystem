package com.phucx.phucxfoodshop.service.jwt.imp;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.service.jwt.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtServiceImp implements JwtService {
    private final String SECRET_KEY = "phucxfoodshop50d643b6-b5ab-44d2-b8bf-9d2695163a31-f2ba3f26-3ce8d";
    
    private byte[] getSecretKey(){
        return SECRET_KEY.getBytes();
    }

    @Override
    public String createToken(String id, Map<String, Object> claims, String subject, Date issuedAt, Date expiryDate){
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .id(id)
            .issuedAt(issuedAt)
            .expiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, getSecretKey())
            .compact();
    }

    @Override
    public Claims extractAllClaims(String token){
        return Jwts.parser()
            .setSigningKey(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getBody();
    }

    @Override
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResovler){
        final Claims claims = extractAllClaims(token);
        return claimsResovler.apply(claims);
    }

    @Override
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
}
