package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.constant.RoleName;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RoleUtils {
    public static List<RoleName> getRoles(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> RoleName.valueOf(authority.substring("ROLE_".length())))
                .collect(Collectors.toList());
    }
}
