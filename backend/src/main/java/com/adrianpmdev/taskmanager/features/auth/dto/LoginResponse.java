package com.adrianpmdev.taskmanager.features.auth.dto;

import java.time.Instant;

/**
 * Access token returned after a successful sign in.
 *
 * @param accessToken Bearer token for the following requests
 * @param expiresAt   moment the token stops being valid
 * @author adrianpmdev
 */
public record LoginResponse(String accessToken, Instant expiresAt) {
}
