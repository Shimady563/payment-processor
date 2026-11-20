package com.shimady.payment.processor.security.authentication;

import com.shimady.payment.processor.security.principal.UserPrincipal;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

/**
 * Resource-server authentication whose principal is application user data,
 * while retaining the validated JWT as credentials for infrastructure use.
 */
public final class UserAuthenticationToken extends AbstractAuthenticationToken {
    private final Jwt jwt;
    private final UserPrincipal principal;

    public UserAuthenticationToken(Jwt jwt,
                                   UserPrincipal principal,
                                   Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwt = jwt;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Jwt getCredentials() {
        return jwt;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return principal;
    }

    @Override
    public @NonNull String getName() {
        return principal.username();
    }
}
