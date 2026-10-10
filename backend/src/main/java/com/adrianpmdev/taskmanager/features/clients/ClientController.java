package com.adrianpmdev.taskmanager.features.clients;

import com.adrianpmdev.taskmanager.features.clients.dto.ClientRequest;
import com.adrianpmdev.taskmanager.features.clients.dto.ClientResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoints of the clients.
 *
 * @author adrianpmdev
 */
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    /**
     * Builds the controller with its dependencies.
     *
     * @param clientService client service
     */
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Lists every client.
     *
     * @return clients sorted by name
     */
    @GetMapping
    public List<ClientResponse> findAll() {
        return clientService.findAll();
    }

    /**
     * Gets a client.
     *
     * @param id client identifier
     * @return client
     */
    @GetMapping("/{id}")
    public ClientResponse findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    /**
     * Creates a client.
     *
     * @param request client data
     * @return created client
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@Valid @RequestBody ClientRequest request) {
        return clientService.create(request);
    }

    /**
     * Updates a client.
     *
     * @param id      client identifier
     * @param request new client data
     * @return updated client
     */
    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return clientService.update(id, request);
    }

    /**
     * Deletes a client.
     *
     * @param id client identifier
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}
