package com.phucx.phucxfandb.service.jwt.imp;

import com.phucx.phucxfandb.constant.JwtType;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.exception.InvalidTokenException;
import com.phucx.phucxfandb.service.jwt.JwtEmailService;
import com.phucx.phucxfandb.service.user.UserReaderService;
import com.phucx.phucxfandb.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.phucx.phucxfandb.constant.JwtClaims.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtEmailServiceImpl implements JwtEmailService {

    @Value("${security.jwt.email.expiration-time}")
    private long EMAIL_TOKEN_EXPIRATION_TIME;

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    private final UserReaderService userReaderService;

    @Override
    public String extractEmail(String token) {
        return JwtUtils.extractAllClaims(token, JwtUtils.getSecretKey(jwtSecretKey))
                .get(EMAIL_CLAIM, String.class);
    }

    @Override
    public String generateToken(String email, JwtType type) {
        User user = userReaderService.getUserEntityByEmail(email);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EMAIL_TOKEN_EXPIRATION_TIME);
        return Jwts.builder()
                .signWith(JwtUtils.getSecretKey(jwtSecretKey))
                .subject(user.getUserId())
                .issuedAt(now)
                .expiration(expiry)
                .claim(USERNAME_CLAIM, user.getUsername())
                .claim(EMAIL_CLAIM, user.getEmail())
                .claim(TYPE_CLAIM, type.name())
                .compact();
    }

    @Override
    public void validateToken(String token, JwtType type) {
        Claims claims = JwtUtils.extractAllClaims(token, JwtUtils.getSecretKey(jwtSecretKey));

        Date expiration = claims.getExpiration();
        if(expiration.after(new Date())){
            throw new InvalidTokenException("Token is expired");
        }

        JwtType jwtType = JwtType.valueOf(claims.get(TYPE_CLAIM, String.class));
        if (!jwtType.equals(type)) {
            throw new InvalidTokenException("Invalid token type");
        }
    }
}
