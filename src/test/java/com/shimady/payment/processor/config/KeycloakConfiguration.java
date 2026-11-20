package com.shimady.payment.processor.config;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface KeycloakConfiguration {
    String REALM_NAME = "test";

    @Container
    KeycloakContainer KEYCLOAK = new KeycloakContainer(DockerImageName.parse("quay.io/keycloak/keycloak:26.8"))
            .withRealmImportFile("init/test-realm.json");

    @DynamicPropertySource
    static void dynamicPropertyRegistration(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> KEYCLOAK.getIssuerUrl(REALM_NAME));
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> KEYCLOAK.getJwksUri(REALM_NAME));
    }
}
