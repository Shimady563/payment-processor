package com.shimady.payment.processor.security.converter;

import com.shimady.payment.processor.config.properties.JwtAuthConverterProperties;
import com.shimady.payment.processor.security.authentication.UserAuthenticationToken;
import com.shimady.payment.processor.security.permission.UserPermissions;
import com.shimady.payment.processor.security.permission.UserPermissionsProvider;
import com.shimady.payment.processor.security.principal.KeycloakUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts keycloak jwt token into application authentication token.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtAuthConverterProperties properties;
    private final UserPermissionsProvider permissionsProvider;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Set<UserPermissions> userPermissions = permissionsProvider.map(extractResourceRoles(source).toList());
        Collection<? extends GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(source).stream(),
                userPermissions.stream()
        ).collect(Collectors.toSet());
        return new UserAuthenticationToken(source, userPrincipal(source, userPermissions), authorities);
    }

    private KeycloakUserPrincipal userPrincipal(@NonNull Jwt source,
                                                Set<UserPermissions> userPermissions) {
        String username = source.getClaimAsString(properties.getPrincipalAttribute());
        if (username == null || username.isBlank()) {
            username = source.getSubject();
        }
        return new KeycloakUserPrincipal(
                source.getSubject(),
                username,
                source.getClaimAsString("email"),
                source.getClaimAsString("given_name"),
                source.getClaimAsString("family_name"),
                source.getClaimAsString("name"),
                Boolean.TRUE.equals(source.getClaim("email_verified")),
                userPermissions);
    }

    private Stream<String> extractResourceRoles(@NonNull Jwt source) {
        Map<String, Object> resourceAccess = source.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(properties.getResourceId())) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Stream.empty();
        }
        return resourceRoles.stream()
                .filter(java.util.Objects::nonNull);
    }
}
