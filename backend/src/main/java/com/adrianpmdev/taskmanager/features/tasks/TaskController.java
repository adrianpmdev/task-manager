package com.adrianpmdev.taskmanager.features.tasks;

import com.adrianpmdev.taskmanager.features.tasks.dto.TaskRequest;
import com.adrianpmdev.taskmanager.features.tasks.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoints of the tasks. Tasks are listed and created under their client and edited by id.
 *
 * @author adrianpmdev
 */
@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;

    /**
     * Builds the controller with its dependencies.
     *
     * @param taskService task service
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Lists the tasks of a client.
     *
     * @param clientId client identifier
     * @param status   optional status filter
     * @return tasks, closest due date first
     */
    @GetMapping("/clients/{clientId}/tasks")
    public List<TaskResponse> findByClient(@PathVariable Long clientId,
                                           @RequestParam(required = false) TaskStatus status) {
        return taskService.findByClient(clientId, status);
    }

    /**
     * Creates a task for a client.
     *
     * @param clientId client identifier
     * @param request  task data
     * @return created task
     */
    @PostMapping("/clients/{clientId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@PathVariable Long clientId, @Valid @RequestBody TaskRequest request) {
        return taskService.create(clientId, request);
    }

    /**
     * Updates a task.
     *
     * @param id      task identifier
     * @param request new task data
     * @return updated task
     */
    @PutMapping("/tasks/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    /**
     * Deletes a task.
     *
     * @param id task identifier
     */
    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
