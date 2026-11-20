package com.shimady.payment.processor.security.principal;

import com.shimady.payment.processor.security.permission.UserPermissions;

import java.util.Set;

/**
 * Application user principal representing keycloak user info.
 */
public record KeycloakUserPrincipal(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String fullName,
        boolean emailVerified,
        Set<UserPermissions> roles
) implements UserPrincipal {

    public KeycloakUserPrincipal {
        roles = Set.copyOf(roles);
    }
}
