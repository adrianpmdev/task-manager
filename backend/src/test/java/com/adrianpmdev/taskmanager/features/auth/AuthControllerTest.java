package com.adrianpmdev.taskmanager.features.auth;

import com.adrianpmdev.taskmanager.config.JwtProperties;
import com.adrianpmdev.taskmanager.features.users.AppUser;
import com.adrianpmdev.taskmanager.features.users.AppUserRepository;
import com.adrianpmdev.taskmanager.security.JwtConfig;
import com.adrianpmdev.taskmanager.security.TokenService;
import com.adrianpmdev.taskmanager.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Login tests through the controller, with the real password hashing and token signing.
 * Only the user repository is mocked.
 *
 * @author adrianpmdev
 */
class AuthControllerTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "test-password";
    private static final Pattern ACCESS_TOKEN = Pattern.compile("\"accessToken\"\\s*:\\s*\"([^\"]+)\"");

    private final JwtConfig jwtConfig = new JwtConfig();
    private final JwtProperties properties =
            new JwtProperties("test-secret-with-at-least-32-bytes!!", 60, "task-manager-test");
    private final AppUserRepository userRepository = mock(AppUserRepository.class);

    private JwtDecoder jwtDecoder;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = jwtConfig.passwordEncoder();
        TokenService tokenService = new TokenService(jwtConfig.jwtEncoder(properties), properties);
        AuthService authService = new AuthService(userRepository, passwordEncoder, tokenService);

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(new AppUser(USERNAME, passwordEncoder.encode(PASSWORD))));

        jwtDecoder = jwtConfig.jwtDecoder(properties);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void loginWithValidCredentialsReturnsVerifiableToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(USERNAME, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresAt").exists())
                .andReturn();

        Jwt jwt = jwtDecoder.decode(extractToken(result.getResponse().getContentAsString()));

        assertThat(jwt.getSubject()).isEqualTo(USERNAME);
        assertThat(jwt.getClaimAsString("iss")).isEqualTo("task-manager-test");
    }

    @Test
    void loginWithWrongPasswordReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials(USERNAME, "wrong-password")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithUnknownUserReturnsUnauthorized() throws Exception {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials("ghost", PASSWORD)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginWithBlankFieldsReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentials("", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    private static String credentials(String username, String password) {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }

    private static String extractToken(String body) {
        Matcher matcher = ACCESS_TOKEN.matcher(body);
        assertThat(matcher.find()).as("response contains accessToken").isTrue();

        return matcher.group(1);
    }
}
