/**
 * TaskApi
 *
 * Calls to the task endpoints of the API.
 *
 * @author adrianpmdev
 */

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Task, TaskRequest, TaskStatus } from '@shared/models/task';
import { API_BASE_URL } from '../api/api';

@Injectable({ providedIn: 'root' })
export class TaskApi {
    private readonly http = inject(HttpClient);

    /** Lists the tasks of a client, optionally filtered by status. */
    findByClient(clientId: number, status: TaskStatus | null): Observable<Task[]> {
        const params = status ? new HttpParams().set('status', status) : undefined;

        return this.http.get<Task[]>(`${API_BASE_URL}/clients/${clientId}/tasks`, { params });
    }

    /** Creates a task for a client. */
    create(clientId: number, request: TaskRequest): Observable<Task> {
        return this.http.post<Task>(`${API_BASE_URL}/clients/${clientId}/tasks`, request);
    }

    /** Updates a task. */
    update(id: number, request: TaskRequest): Observable<Task> {
        return this.http.put<Task>(`${API_BASE_URL}/tasks/${id}`, request);
    }

    /** Deletes a task. */
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${API_BASE_URL}/tasks/${id}`);
    }
}
