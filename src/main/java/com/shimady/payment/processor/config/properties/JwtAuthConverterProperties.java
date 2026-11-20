package com.shimady.payment.processor.config.properties;

import com.shimady.payment.processor.security.permission.UserPermissions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Set;


/**
 * Properties supporting conversion of keycloak jwt token into application authentication token.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("security.jwt.auth.converter")
public class JwtAuthConverterProperties {

    @NotBlank
    private String resourceId;

    @NotBlank
    private String principalAttribute;

    @NotEmpty
    private Map<String, Set<UserPermissions>> rolesToPermissions = Map.of();
}
