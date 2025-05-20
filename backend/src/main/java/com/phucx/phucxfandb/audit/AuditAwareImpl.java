package com.phucx.phucxfandb.audit;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "auditorAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken))
                .map(Authentication::getName)
                .orElse("DEFAULT_SYSTEM")
                .describeConstable();
    }
}
