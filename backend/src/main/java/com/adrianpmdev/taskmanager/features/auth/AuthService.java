package com.adrianpmdev.taskmanager.features.auth;

import com.adrianpmdev.taskmanager.features.auth.dto.LoginRequest;
import com.adrianpmdev.taskmanager.features.auth.dto.LoginResponse;
import com.adrianpmdev.taskmanager.features.users.AppUser;
import com.adrianpmdev.taskmanager.features.users.AppUserRepository;
import com.adrianpmdev.taskmanager.security.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Checks the credentials of a user and issues their access token.
 *
 * @author adrianpmdev
 */
@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS = "Invalid username or password";

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Builds the service with its dependencies.
     *
     * @param userRepository  user persistence
     * @param passwordEncoder password hasher
     * @param tokenService    token issuer
     */
    public AuthService(AppUserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Signs a user in. Unknown users and wrong passwords get the same error, so the API never reveals
     * which usernames exist.
     *
     * @param request credentials
     * @return access token and its expiry
     * @throws BadCredentialsException if the credentials are not valid
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .filter(found -> passwordEncoder.matches(request.password(), found.getPasswordHash()))
                .orElseThrow(() -> new BadCredentialsException(INVALID_CREDENTIALS));

        TokenService.IssuedToken token = tokenService.issue(user.getUsername());

        return new LoginResponse(token.value(), token.expiresAt());
    }
}
