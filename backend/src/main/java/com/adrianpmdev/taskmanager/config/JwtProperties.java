package com.adrianpmdev.taskmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;

/**
 * Settings of the access tokens issued by this backend ({@code app.jwt.*}).
 *
 * @param secret            HMAC secret used to sign and verify the tokens (at least 32 bytes for HS256)
 * @param expirationMinutes minutes a token stays valid
 * @param issuer            value of the {@code iss} claim
 * @author adrianpmdev
 */
@ConfigurationProperties("app.jwt")
public record JwtProperties(String secret, long expirationMinutes, String issuer) {

    private static final int MIN_SECRET_BYTES = 32;

    /**
     * Fails on startup instead of issuing weak or unverifiable tokens.
     */
    public JwtProperties {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalStateException("app.jwt.secret must be at least " + MIN_SECRET_BYTES + " bytes long");
        }

        if (expirationMinutes <= 0) {
            throw new IllegalStateException("app.jwt.expiration-minutes must be greater than zero");
        }
    }
}
