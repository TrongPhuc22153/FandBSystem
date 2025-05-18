package com.phucx.phucxfandb.service.jwt.imp;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.service.jwt.JwtAuthenticationService;
import com.phucx.phucxfandb.utils.RoleUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {
    private final String USERNAME_CLAIM = "username";
    private final String EMAIL_CLAIM = "email";
    private final String ROLE_CLAIM = "role";

    @Value("${security.jwt.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;
    
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAuthToken(User user) {
        log.debug("generateAuthToken(username={})", user.getUsername());
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(user.getUserId())
                .issuedAt(now)
                .expiration(expiry)
                .claim(USERNAME_CLAIM, user.getUsername())
                .claim(EMAIL_CLAIM, user.getEmail())
                .claim(ROLE_CLAIM, roles)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        log.debug("generateAuthToken(token={})", token);
        return extractAllClaims(token)
                .get(USERNAME_CLAIM, String.class);
    }

    @Override
    public Set<RoleName> extractRoles(String token) {
        log.debug("extractRoles(token={})", token);
        try {
            Collection<?> roles = extractAllClaims(token).get(ROLE_CLAIM, Collection.class);
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
        log.debug("validateToken(token={})", token);
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        log.debug("extractAllClaims(token={})", token);
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}
