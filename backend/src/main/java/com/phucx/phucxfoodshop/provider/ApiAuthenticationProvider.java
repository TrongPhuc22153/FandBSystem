package com.phucx.phucxfoodshop.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.phucx.phucxfoodshop.model.UserSysDetails;
import com.phucx.phucxfoodshop.service.user.UserSysDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserSysDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("ApiAuthenticationProvider");
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        UserSysDetails user = (UserSysDetails) userDetailsService.loadUserByUsername(username);
        Boolean check = userDetailsService.checkUserAuthentication(password, user.getPassword());
        if(!check){
            throw new BadCredentialsException ("Invalid username or password!");
        }
        // check enable of an account
        if(!user.getUser().getEnabled()){
            throw new DisabledException("Your account " + username + " has been disabled");
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
        log.info("authenticatoin class: {}", authentication.getName());
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
    
}
