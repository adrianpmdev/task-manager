package com.adrianpmdev.taskmanager.features.clients.dto;

import com.adrianpmdev.taskmanager.features.clients.Client;

import java.time.Instant;

/**
 * Client returned by the API.
 *
 * @param id        identifier
 * @param name      name
 * @param email     contact email
 * @param phone     contact phone
 * @param createdAt creation date
 * @author adrianpmdev
 */
public record ClientResponse(Long id, String name, String email, String phone, Instant createdAt) {

    /**
     * Maps an entity to its response.
     *
     * @param client entity
     * @return response
     */
    public static ClientResponse from(Client client) {
        return new ClientResponse(client.getId(), client.getName(), client.getEmail(), client.getPhone(),
                client.getCreatedAt());
    }
}
