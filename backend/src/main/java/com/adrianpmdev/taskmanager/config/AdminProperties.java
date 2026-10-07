package com.adrianpmdev.taskmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Credentials of the internal user created on first start ({@code app.admin.*}).
 *
 * @param username login name
 * @param password plain password, only read to store its hash
 * @author adrianpmdev
 */
@ConfigurationProperties("app.admin")
public record AdminProperties(String username, String password) {

    /**
     * Fails on startup when the credentials are missing.
     */
    public AdminProperties {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalStateException("app.admin.username and app.admin.password are required");
        }
    }
}
