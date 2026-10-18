package com.adrianpmdev.taskmanager.features.clients;

import com.adrianpmdev.taskmanager.features.clients.dto.ClientRequest;
import com.adrianpmdev.taskmanager.features.clients.dto.ClientResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests of {@link ClientService}.
 *
 * @author adrianpmdev
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void findAllReturnsClientsInRepositoryOrder() {
        when(clientRepository.findAllByOrderByNameAsc()).thenReturn(List.of(
                new Client("Acme", null, null),
                new Client("Globex", "info@globex.test", "600000000")));

        List<ClientResponse> clients = clientService.findAll();

        assertThat(clients).extracting(ClientResponse::name).containsExactly("Acme", "Globex");
    }

    @Test
    void createSavesClientWithRequestData() {
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientResponse created = clientService.create(new ClientRequest("Acme", "info@acme.test", "600000000"));

        assertThat(created.name()).isEqualTo("Acme");
        assertThat(created.email()).isEqualTo("info@acme.test");
        assertThat(created.phone()).isEqualTo("600000000");
    }

    @Test
    void updateReplacesClientData() {
        Client client = new Client("Acme", null, null);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientResponse updated = clientService.update(1L, new ClientRequest("Acme Corp", "hello@acme.test", null));

        assertThat(updated.name()).isEqualTo("Acme Corp");
        assertThat(updated.email()).isEqualTo("hello@acme.test");
        assertThat(client.getName()).isEqualTo("Acme Corp");
    }

    @Test
    void updateFailsWhenClientDoesNotExist() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.update(99L, new ClientRequest("Acme", null, null)))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteRemovesExistingClient() {
        Client client = new Client("Acme", null, null);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.delete(1L);

        verify(clientRepository).delete(client);
    }

    @Test
    void deleteFailsWhenClientDoesNotExist() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.delete(99L)).isInstanceOf(NoSuchElementException.class);
        verify(clientRepository, never()).delete(any(Client.class));
    }
}
