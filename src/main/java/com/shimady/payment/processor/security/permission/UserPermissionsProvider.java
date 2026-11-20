package com.shimady.payment.processor.security.permission;

import com.shimady.payment.processor.config.properties.JwtAuthConverterProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Maps external Keycloak role names to application permissions.
 */
@Service
@RequiredArgsConstructor
public class UserPermissionsProvider {
    private final JwtAuthConverterProperties properties;

    public Set<UserPermissions> map(Collection<String> keycloakRoles) {
        Map<String, Set<UserPermissions>> rolesToPermissions = properties.getRolesToPermissions();
        return keycloakRoles.stream()
                .map(rolesToPermissions::get)
                .filter(permissions -> !CollectionUtils.isEmpty(permissions))
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }
}
