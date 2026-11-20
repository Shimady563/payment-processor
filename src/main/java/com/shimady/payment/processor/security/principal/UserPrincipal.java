package com.shimady.payment.processor.security.principal;

import com.shimady.payment.processor.security.permission.UserPermissions;

import java.security.Principal;
import java.util.Set;

/**
 * Authenticated user data made available to application code.
 */
public interface UserPrincipal extends Principal {

    String id();

    String username();

    String email();

    String firstName();

    String lastName();

    String fullName();

    boolean emailVerified();

    Set<UserPermissions> roles();

    @Override
    default String getName() {
        return username();
    }
}
