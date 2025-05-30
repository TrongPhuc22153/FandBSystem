package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.enums.RoleName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleUtils {
    public static List<RoleName> getRoles(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> RoleName.valueOf(authority.substring("ROLE_".length())))
                .collect(Collectors.toList());
    }

    public static Collection<GrantedAuthority> getAuthorities(Set<RoleName> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
