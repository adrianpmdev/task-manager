package com.adrianpmdev.taskmanager.features.tasks.dto;

import com.adrianpmdev.taskmanager.features.tasks.Task;
import com.adrianpmdev.taskmanager.features.tasks.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Task returned by the API.
 *
 * @param id          identifier
 * @param clientId    owner client identifier
 * @param title       title
 * @param status      status
 * @param dueDate     due date
 * @param createdAt   creation date
 * @author adrianpmdev
 */
public record TaskResponse(Long id, Long clientId, String title, TaskStatus status, LocalDate dueDate,
                           Instant createdAt) {

    /**
     * Maps an entity to its response.
     *
     * @param task entity
     * @return response
     */
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getClient().getId(), task.getTitle(), task.getStatus(),
                task.getDueDate(), task.getCreatedAt());
    }
}
