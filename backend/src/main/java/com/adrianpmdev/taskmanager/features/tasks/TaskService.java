package com.adrianpmdev.taskmanager.features.tasks;

import com.adrianpmdev.taskmanager.features.clients.Client;
import com.adrianpmdev.taskmanager.features.clients.ClientService;
import com.adrianpmdev.taskmanager.features.tasks.dto.TaskRequest;
import com.adrianpmdev.taskmanager.features.tasks.dto.TaskResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Use cases of the tasks of each client.
 *
 * @author adrianpmdev
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ClientService clientService;

    /**
     * Builds the service with its dependencies.
     *
     * @param taskRepository task persistence
     * @param clientService  client service, to check that the client exists
     */
    public TaskService(TaskRepository taskRepository, ClientService clientService) {
        this.taskRepository = taskRepository;
        this.clientService = clientService;
    }

    /**
     * Lists the tasks of a client, optionally filtered by status.
     *
     * @param clientId client identifier
     * @param status   status to filter by, or {@code null} for every task
     * @return tasks, closest due date first
     * @throws NoSuchElementException if the client does not exist
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> findByClient(Long clientId, TaskStatus status) {
        clientService.getClient(clientId);

        List<Task> tasks = status == null
                ? taskRepository.findByClientIdOrderByDueDateAsc(clientId)
                : taskRepository.findByClientIdAndStatusOrderByDueDateAsc(clientId, status);

        return tasks.stream().map(TaskResponse::from).toList();
    }

    /**
     * Creates a task for a client.
     *
     * @param clientId client identifier
     * @param request  task data
     * @return created task
     * @throws NoSuchElementException if the client does not exist
     */
    @Transactional
    public TaskResponse create(Long clientId, TaskRequest request) {
        Client client = clientService.getClient(clientId);
        Task task = new Task(client, request.title(), request.status(), request.dueDate());

        return TaskResponse.from(taskRepository.save(task));
    }

    /**
     * Updates a task.
     *
     * @param id      task identifier
     * @param request new task data
     * @return updated task
     * @throws NoSuchElementException if the task does not exist
     */
    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = getTask(id);
        task.update(request.title(), request.status(), request.dueDate());

        return TaskResponse.from(task);
    }

    /**
     * Deletes a task.
     *
     * @param id task identifier
     * @throws NoSuchElementException if the task does not exist
     */
    @Transactional
    public void delete(Long id) {
        taskRepository.delete(getTask(id));
    }

    private Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task " + id + " not found"));
    }
}
