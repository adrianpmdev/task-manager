package com.adrianpmdev.taskmanager.features.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Persistence of the application users.
 *
 * @author adrianpmdev
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Finds a user by login name.
     *
     * @param username login name
     * @return the user, if it exists
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Checks whether a login name is already taken.
     *
     * @param username login name
     * @return {@code true} if a user with that name exists
     */
    boolean existsByUsername(String username);
}
