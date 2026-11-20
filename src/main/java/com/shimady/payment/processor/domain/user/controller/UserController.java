package com.shimady.payment.processor.domain.user.controller;

import com.shimady.payment.processor.config.SwaggerConfig;
import com.shimady.payment.processor.domain.common.controller.dto.AppError;
import com.shimady.payment.processor.domain.user.controller.dto.UserDto;
import com.shimady.payment.processor.domain.user.controller.mapper.UserMapper;
import com.shimady.payment.processor.security.principal.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shimady.payment.processor.config.SwaggerConfig.JWT;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = SwaggerConfig.Tags.USER, description = "Authenticated user operations")
@SecurityRequirement(name = JWT)
public class UserController {
    private final UserMapper userMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_USER_INFO')")
    @Operation(
            summary = "Get the current user",
            description = "Returns the details of the user represented by the bearer token.",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Current user information",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Missing, expired, or invalid bearer token",
                    content = @Content(schema = @Schema(implementation = AppError.class))
            )
    })
    public UserDto getUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal() UserPrincipal user) {
        log.debug("Getting user info. Username [{}]", user.username());
        return userMapper.map(user);
    }
}
