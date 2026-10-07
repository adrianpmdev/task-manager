package com.adrianpmdev.taskmanager.features.users;

import com.adrianpmdev.taskmanager.config.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Creates the internal user from {@code app.admin.*} on startup when it does not exist yet.
 * An existing user is never modified, so changing the configured password later has no effect.
 *
 * @author adrianpmdev
 */
@Component
public class AdminUserInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    /**
     * Builds the initializer with its dependencies.
     *
     * @param userRepository  user persistence
     * @param passwordEncoder password hasher
     * @param adminProperties configured credentials
     */
    public AdminUserInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder,
                                AdminProperties adminProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByUsername(adminProperties.username())) {
            return;
        }

        userRepository.save(new AppUser(adminProperties.username(), passwordEncoder.encode(adminProperties.password())));
        log.info("Internal user '{}' created", adminProperties.username());
    }
}
