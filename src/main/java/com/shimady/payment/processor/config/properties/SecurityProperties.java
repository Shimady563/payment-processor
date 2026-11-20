package com.shimady.payment.processor.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;


/**
 * Base security properties.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("security")
public class SecurityProperties {

    @NotNull
    private List<String> whitelist = new ArrayList<>();

    @NotNull
    private List<String> allowedOrigins = new ArrayList<>();
}
