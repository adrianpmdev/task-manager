package com.adrianpmdev.taskmanager.security;

import com.adrianpmdev.taskmanager.config.JwtProperties;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

/**
 * Issues signed access tokens for authenticated users.
 *
 * @author adrianpmdev
 */
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties properties;

    /**
     * Builds the service with its dependencies.
     *
     * @param jwtEncoder token encoder
     * @param properties JWT settings
     */
    public TokenService(JwtEncoder jwtEncoder, JwtProperties properties) {
        this.jwtEncoder = jwtEncoder;
        this.properties = properties;
    }

    /**
     * Issues a token for a user.
     *
     * @param username subject of the token
     * @return the token and its expiry
     */
    public IssuedToken issue(String username) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(properties.expirationMinutes()));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .subject(username)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new IssuedToken(token, expiresAt);
    }

    /**
     * Signed token and the moment it stops being valid.
     *
     * @param value     encoded token
     * @param expiresAt expiry
     */
    public record IssuedToken(String value, Instant expiresAt) {
    }
}
