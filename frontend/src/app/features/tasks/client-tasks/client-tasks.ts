/**
 * ClientTasks
 *
 * Tasks of one client: filter by status, create, change status and delete.
 *
 * @author adrianpmdev
 */

import { ChangeDetectionStrategy, Component, OnInit, inject, input, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ClientApi } from '@core/clients/client-api';
import { TaskApi } from '@core/tasks/task-api';
import { Client } from '@shared/models/client';
import { Task, TaskStatus } from '@shared/models/task';

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-client-tasks',
    imports: [ReactiveFormsModule, RouterLink],
    templateUrl: './client-tasks.html',
    styleUrl: './client-tasks.scss',
})
export class ClientTasks implements OnInit {
    private readonly clientApi = inject(ClientApi);
    private readonly taskApi = inject(TaskApi);

    /** Client id taken from the route. */
    readonly id = input.required<string>();

    protected readonly statusLabels: Record<TaskStatus, string> = {
        PENDING: 'Pendiente',
        IN_PROGRESS: 'En curso',
        DONE: 'Hecha',
    };
    protected readonly statuses = Object.keys(this.statusLabels) as TaskStatus[];

    protected readonly client = signal<Client | null>(null);
    protected readonly tasks = signal<Task[]>([]);
    protected readonly filter = signal<TaskStatus | null>(null);
    protected readonly error = signal<string | null>(null);

    protected readonly form = inject(NonNullableFormBuilder).group({
        title: ['', [Validators.required, Validators.maxLength(150)]],
        dueDate: ['', Validators.required],
    });

    ngOnInit(): void {
        this.clientApi.findById(this.clientId()).subscribe({
            next: (client) => this.client.set(client),
            error: () => this.error.set('No se ha encontrado el cliente'),
        });
        this.load();
    }

    protected setFilter(status: string): void {
        this.filter.set(status ? (status as TaskStatus) : null);
        this.load();
    }

    protected create(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        const { title, dueDate } = this.form.getRawValue();
        this.taskApi
            .create(this.clientId(), { title: title.trim(), status: 'PENDING', dueDate })
            .subscribe({
                next: () => {
                    this.form.reset();
                    this.load();
                },
                error: () => this.error.set('No se ha podido crear la tarea'),
            });
    }

    protected changeStatus(task: Task, status: string): void {
        this.taskApi
            .update(task.id, {
                title: task.title,
                status: status as TaskStatus,
                dueDate: task.dueDate,
            })
            .subscribe({
                next: () => this.load(),
                error: () => this.error.set('No se ha podido actualizar la tarea'),
            });
    }

    protected remove(task: Task): void {
        this.taskApi.delete(task.id).subscribe({
            next: () => this.load(),
            error: () => this.error.set('No se ha podido eliminar la tarea'),
        });
    }

    private load(): void {
        this.taskApi.findByClient(this.clientId(), this.filter()).subscribe({
            next: (tasks) => this.tasks.set(tasks),
            error: () => this.error.set('No se han podido cargar las tareas'),
        });
    }

    private clientId(): number {
        return Number(this.id());
    }
}
