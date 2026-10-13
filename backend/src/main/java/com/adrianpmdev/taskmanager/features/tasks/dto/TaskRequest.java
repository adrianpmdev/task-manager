package com.adrianpmdev.taskmanager.features.tasks.dto;

import com.adrianpmdev.taskmanager.features.tasks.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data to create or update a task.
 *
 * @param title       title
 * @param status      status
 * @param dueDate     due date
 * @author adrianpmdev
 */
public record TaskRequest(
        @NotBlank @Size(max = 150) String title,
        @NotNull TaskStatus status,
        @NotNull LocalDate dueDate
) {
}
