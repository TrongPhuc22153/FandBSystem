package com.phucx.phucxfandb.service.user.imp;

import java.util.Collection;
import java.util.stream.Collectors;

import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::fromUser)
                .orElseThrow(()->
                        new UsernameNotFoundException("User does not found!")
                );
    }

    private UserDetails fromUser(User user){
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                        .collect(Collectors.toSet());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isEnabled() {
                return user.getEnabled();
            }
        };
    }
}
