package com.shimady.payment.processor.util;

import lombok.experimental.UtilityClass;

import static com.shimady.payment.processor.config.KeycloakConfiguration.KEYCLOAK;
import static com.shimady.payment.processor.config.KeycloakConfiguration.REALM_NAME;


@UtilityClass
public class KeycloakUtil {
    private static final String CLIENT_ID = "test-client";

    public static String getAccessToken(String username, String password) {
        return KEYCLOAK.getAccessToken(REALM_NAME, CLIENT_ID, username, password);
    }
}
