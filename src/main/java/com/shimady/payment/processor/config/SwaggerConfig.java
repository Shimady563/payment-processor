package com.shimady.payment.processor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import static com.shimady.payment.processor.config.SwaggerConfig.JWT;


@OpenAPIDefinition(
        info = @Info(
                title = "Payment Processor",
                description = "Service for processing payments"
        )
)
@SecurityScheme(
        name = JWT,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = JWT
)
public class SwaggerConfig {
    public static final String JWT = "JWT";

    public static class Tags {
        public static final String USER = "Users";
    }
}
