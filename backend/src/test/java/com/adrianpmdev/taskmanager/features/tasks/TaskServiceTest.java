package com.adrianpmdev.taskmanager.features.tasks;

import com.adrianpmdev.taskmanager.features.clients.Client;
import com.adrianpmdev.taskmanager.features.clients.ClientService;
import com.adrianpmdev.taskmanager.features.tasks.dto.TaskRequest;
import com.adrianpmdev.taskmanager.features.tasks.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests of {@link TaskService}.
 *
 * @author adrianpmdev
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private static final LocalDate DUE_DATE = LocalDate.of(2026, 12, 1);

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private TaskService taskService;

    private final Client client = new Client("Acme", null, null);

    @Test
    void findByClientWithoutStatusReturnsEveryTask() {
        when(clientService.getClient(1L)).thenReturn(client);
        when(taskRepository.findByClientIdOrderByDueDateAsc(1L)).thenReturn(List.of(
                new Task(client, "Invoice", TaskStatus.PENDING, DUE_DATE),
                new Task(client, "Report", TaskStatus.DONE, DUE_DATE.plusDays(1))));

        List<TaskResponse> tasks = taskService.findByClient(1L, null);

        assertThat(tasks).extracting(TaskResponse::title).containsExactly("Invoice", "Report");
        verify(taskRepository, never()).findByClientIdAndStatusOrderByDueDateAsc(any(), any());
    }

    @Test
    void findByClientWithStatusFiltersByStatus() {
        when(clientService.getClient(1L)).thenReturn(client);
        when(taskRepository.findByClientIdAndStatusOrderByDueDateAsc(1L, TaskStatus.DONE)).thenReturn(List.of(
                new Task(client, "Report", TaskStatus.DONE, DUE_DATE)));

        List<TaskResponse> tasks = taskService.findByClient(1L, TaskStatus.DONE);

        assertThat(tasks).singleElement().extracting(TaskResponse::status).isEqualTo(TaskStatus.DONE);
        verify(taskRepository, never()).findByClientIdOrderByDueDateAsc(any());
    }

    @Test
    void findByClientFailsWhenClientDoesNotExist() {
        when(clientService.getClient(99L)).thenThrow(new NoSuchElementException("Client 99 not found"));

        assertThatThrownBy(() -> taskService.findByClient(99L, null)).isInstanceOf(NoSuchElementException.class);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void createSavesTaskForClient() {
        when(clientService.getClient(1L)).thenReturn(client);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse created = taskService.create(1L,
                new TaskRequest("Invoice", TaskStatus.PENDING, DUE_DATE));

        assertThat(created.title()).isEqualTo("Invoice");
        assertThat(created.status()).isEqualTo(TaskStatus.PENDING);
        assertThat(created.dueDate()).isEqualTo(DUE_DATE);
    }

    @Test
    void createFailsWhenClientDoesNotExist() {
        when(clientService.getClient(99L)).thenThrow(new NoSuchElementException("Client 99 not found"));

        assertThatThrownBy(() -> taskService.create(99L,
                new TaskRequest("Invoice", TaskStatus.PENDING, DUE_DATE)))
                .isInstanceOf(NoSuchElementException.class);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateChangesStatusAndData() {
        Task task = new Task(client, "Invoice", TaskStatus.PENDING, DUE_DATE);
        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));

        TaskResponse updated = taskService.update(5L,
                new TaskRequest("Invoice", TaskStatus.DONE, DUE_DATE.plusDays(3)));

        assertThat(updated.status()).isEqualTo(TaskStatus.DONE);
        assertThat(updated.dueDate()).isEqualTo(DUE_DATE.plusDays(3));
    }

    @Test
    void deleteFailsWhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(99L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("99");
        verify(taskRepository, never()).delete(any());
    }
}
