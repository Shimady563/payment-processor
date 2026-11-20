package com.shimady.payment.processor.domain.common.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Application error details")
public record AppError(

        @Schema(description = "Error message", example = "User not found")
        String message,

        @Schema(description = "HTTP status code", example = "404")
        int code
) {

    public static AppError of(int code, String message) {
        return new AppError(message, code);
    }
}
