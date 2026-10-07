package com.adrianpmdev.taskmanager.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credentials sent to sign in.
 *
 * @param username login name
 * @param password password
 * @author adrianpmdev
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
