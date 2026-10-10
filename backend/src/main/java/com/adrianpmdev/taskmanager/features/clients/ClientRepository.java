package com.adrianpmdev.taskmanager.features.clients;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Persistence of the clients.
 *
 * @author adrianpmdev
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Lists every client sorted by name.
     *
     * @return clients
     */
    List<Client> findAllByOrderByNameAsc();
}
