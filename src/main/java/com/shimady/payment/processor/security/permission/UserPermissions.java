package com.shimady.payment.processor.security.permission;

import org.springframework.security.core.GrantedAuthority;

/**
 * Permissions understood by this application.
 */
public enum UserPermissions implements GrantedAuthority {
    READ_USER_INFO;

    @Override
    public String getAuthority() {
        return name();
    }
}
