package com.phucx.phucxfandb.service.jwt.imp;

import com.phucx.phucxfandb.constant.JwtType;
import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.service.jwt.JwtAuthenticationService;
import com.phucx.phucxfandb.utils.JwtUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.phucx.phucxfandb.constant.JwtClaims.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {
    @Value("${security.jwt.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Override
    public String generateAuthToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        return Jwts.builder()
                .signWith(JwtUtils.getSecretKey(jwtSecretKey))
                .subject(user.getUserId())
                .issuedAt(now)
                .expiration(expiry)
                .claim(USERNAME_CLAIM, user.getUsername())
                .claim(EMAIL_CLAIM, user.getEmail())
                .claim(TYPE_CLAIM, JwtType.BEARER)
                .claim(ROLE_CLAIM, roles)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return JwtUtils.extractAllClaims(token, JwtUtils.getSecretKey(jwtSecretKey))
                .get(USERNAME_CLAIM, String.class);
    }

    @Override
    public Set<RoleName> extractRoles(String token) {
        try {
            Collection<?> roles = JwtUtils.extractAllClaims(token, JwtUtils.getSecretKey(jwtSecretKey)).get(ROLE_CLAIM, Collection.class);
            if (roles != null) {
                return roles.stream()
                        .map(Object::toString)
                        .map(RoleName::valueOf)
                        .collect(Collectors.toSet());
            }
        } catch (Exception e) {
            log.warn("Error: {}", e.getMessage());
        }
        return Collections.emptySet();
    }

    @Override
    public UsernamePasswordAuthenticationToken extractAuthentication(String token) {
        String username = this.extractUsername(token);
        Set<RoleName> roles = this.extractRoles(token);
        Collection<GrantedAuthority> authorities = RoleUtils.getAuthorities(roles);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            Claims claims = JwtUtils.extractAllClaims(token, JwtUtils.getSecretKey(jwtSecretKey));

            JwtType jwtType = JwtType.valueOf(claims.get(TYPE_CLAIM, String.class));
            if (!jwtType.equals(JwtType.BEARER)) {
                throw new IllegalArgumentException("Invalid action type");
            }

            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
