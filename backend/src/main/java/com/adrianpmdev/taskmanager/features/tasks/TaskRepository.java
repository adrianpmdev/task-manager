package com.adrianpmdev.taskmanager.features.tasks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Persistence of the tasks.
 *
 * @author adrianpmdev
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Lists the tasks of a client, closest due date first.
     *
     * @param clientId client identifier
     * @return tasks
     */
    List<Task> findByClientIdOrderByDueDateAsc(Long clientId);

    /**
     * Lists the tasks of a client with a given status, closest due date first.
     *
     * @param clientId client identifier
     * @param status   status to filter by
     * @return tasks
     */
    List<Task> findByClientIdAndStatusOrderByDueDateAsc(Long clientId, TaskStatus status);
}
