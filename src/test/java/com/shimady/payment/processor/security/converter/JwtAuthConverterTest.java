package com.shimady.payment.processor.security.converter;

import com.shimady.payment.processor.config.properties.JwtAuthConverterProperties;
import com.shimady.payment.processor.security.permission.UserPermissions;
import com.shimady.payment.processor.security.permission.UserPermissionsProvider;
import com.shimady.payment.processor.security.principal.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtAuthConverterTest {

    private final JwtAuthConverter converter = converter();

    private static JwtAuthConverter converter() {
        JwtAuthConverterProperties properties = properties();
        return new JwtAuthConverter(properties, new UserPermissionsProvider(properties));
    }

    private static JwtAuthConverterProperties properties() {
        JwtAuthConverterProperties properties = new JwtAuthConverterProperties();
        properties.setResourceId("payment-processor");
        properties.setPrincipalAttribute("preferred_username");
        properties.setRolesToPermissions(Map.of("user", Set.of(UserPermissions.READ_USER_INFO)));
        return properties;
    }

    @Test
    void createsApplicationUserPrincipalFromKeycloakClaims() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("keycloak-user-id")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .claim("preferred_username", "ada")
                .claim("email", "ada@example.com")
                .claim("given_name", "Ada")
                .claim("family_name", "Lovelace")
                .claim("name", "Ada Lovelace")
                .claim("email_verified", true)
                .claim("resource_access", Map.of(
                        "payment-processor", Map.of("roles", java.util.List.of("user"))))
                .build();

        Authentication authentication = converter.convert(jwt);

        assertNotNull(authentication);
        assertThat(authentication.getPrincipal()).isInstanceOf(UserPrincipal.class);
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        assertThat(user.id()).isEqualTo("keycloak-user-id");
        assertThat(user.username()).isEqualTo("ada");
        assertThat(user.email()).isEqualTo("ada@example.com");
        assertThat(user.firstName()).isEqualTo("Ada");
        assertThat(user.lastName()).isEqualTo("Lovelace");
        assertThat(user.fullName()).isEqualTo("Ada Lovelace");
        assertThat(user.emailVerified()).isTrue();
        assertThat(user.roles()).containsExactly(UserPermissions.READ_USER_INFO);
        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("READ_USER_INFO");
        assertThat(authentication.getName()).isEqualTo("ada");
    }
}
