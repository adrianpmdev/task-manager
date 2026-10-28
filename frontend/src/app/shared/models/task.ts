/**
 * Task models
 *
 * Shapes of the tasks exchanged with the API.
 *
 * @author adrianpmdev
 */

export type TaskStatus = 'PENDING' | 'IN_PROGRESS' | 'DONE';

export interface Task {
    id: number;
    clientId: number;
    title: string;
    status: TaskStatus;
    dueDate: string;
    createdAt: string;
}

export interface TaskRequest {
    title: string;
    status: TaskStatus;
    dueDate: string;
}
