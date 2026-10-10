package com.adrianpmdev.taskmanager.features.clients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data to create or update a client.
 *
 * @param name  name
 * @param email contact email, optional
 * @param phone contact phone, optional
 * @author adrianpmdev
 */
public record ClientRequest(
        @NotBlank @Size(max = 120) String name,
        @Email @Size(max = 120) String email,
        @Size(max = 30) String phone
) {
}
