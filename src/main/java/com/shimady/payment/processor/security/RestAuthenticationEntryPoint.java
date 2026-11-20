package com.shimady.payment.processor.security;

import com.shimady.payment.processor.domain.common.controller.dto.AppError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Converts unauthenticated security failures, including JWT failures, to HTTP 401.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         HttpServletResponse response,
                         @NonNull AuthenticationException exception)
            throws IOException {
        log.debug("An authentication error occurred: ", exception);
        int code = HttpServletResponse.SC_UNAUTHORIZED;
        response.setStatus(code);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(AppError.of(code, exception.getMessage())));
    }
}
