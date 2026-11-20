package com.shimady.payment.processor.domain.user.controller.dto;

import com.shimady.payment.processor.security.permission.UserPermissions;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Information about the authenticated user")
public record UserDto(
        @Schema(description = "User identifier", example = "e7677d49-1d1c-4a9f-a0f5-bc8d2776c25f",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String id,

        @Schema(description = "Username", example = "ada.lovelace", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,

        @Schema(description = "Email address", example = "ada@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,

        @Schema(description = "First name", example = "Ada", requiredMode = Schema.RequiredMode.REQUIRED)
        String firstName,

        @Schema(description = "Last name", example = "Lovelace", requiredMode = Schema.RequiredMode.REQUIRED)
        String lastName,

        @Schema(description = "Full display name", example = "Ada Lovelace",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String fullName,

        @Schema(description = "Whether user has verified email address", example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED)
        boolean emailVerified,

        @Schema(description = "Roles recognized by this application", example = "[USER]",
                requiredMode = Schema.RequiredMode.REQUIRED)
        Set<UserPermissions> roles
) {
}
