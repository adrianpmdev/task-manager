package com.adrianpmdev.taskmanager.features.clients;

import com.adrianpmdev.taskmanager.features.clients.dto.ClientRequest;
import com.adrianpmdev.taskmanager.features.clients.dto.ClientResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Use cases of the clients.
 *
 * @author adrianpmdev
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Builds the service with its dependencies.
     *
     * @param clientRepository client persistence
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Lists every client sorted by name.
     *
     * @return clients
     */
    @Transactional(readOnly = true)
    public List<ClientResponse> findAll() {
        return clientRepository.findAllByOrderByNameAsc().stream()
                .map(ClientResponse::from)
                .toList();
    }

    /**
     * Gets a client.
     *
     * @param id client identifier
     * @return client
     * @throws NoSuchElementException if the client does not exist
     */
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        return ClientResponse.from(getClient(id));
    }

    /**
     * Creates a client.
     *
     * @param request client data
     * @return created client
     */
    @Transactional
    public ClientResponse create(ClientRequest request) {
        Client client = new Client(request.name(), request.email(), request.phone());

        return ClientResponse.from(clientRepository.save(client));
    }

    /**
     * Updates a client.
     *
     * @param id      client identifier
     * @param request new client data
     * @return updated client
     * @throws NoSuchElementException if the client does not exist
     */
    @Transactional
    public ClientResponse update(Long id, ClientRequest request) {
        Client client = getClient(id);
        client.update(request.name(), request.email(), request.phone());

        return ClientResponse.from(client);
    }

    /**
     * Deletes a client.
     *
     * @param id client identifier
     * @throws NoSuchElementException if the client does not exist
     */
    @Transactional
    public void delete(Long id) {
        clientRepository.delete(getClient(id));
    }

    /**
     * Loads a client entity.
     *
     * @param id client identifier
     * @return client entity
     * @throws NoSuchElementException if the client does not exist
     */
    @Transactional(readOnly = true)
    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client " + id + " not found"));
    }
}
