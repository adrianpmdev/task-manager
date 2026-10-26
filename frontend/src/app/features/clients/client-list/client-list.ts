/**
 * ClientList
 *
 * Lists the clients and lets the user create, edit and delete them.
 *
 * @author adrianpmdev
 */

import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ClientApi } from '@core/clients/client-api';
import { Client, ClientRequest } from '@shared/models/client';

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-client-list',
    imports: [ReactiveFormsModule, RouterLink],
    templateUrl: './client-list.html',
    styleUrl: './client-list.scss',
})
export class ClientList {
    private readonly clientApi = inject(ClientApi);

    protected readonly clients = signal<Client[]>([]);
    protected readonly loading = signal(true);
    protected readonly editingId = signal<number | null>(null);
    protected readonly saving = signal(false);
    protected readonly error = signal<string | null>(null);

    protected readonly form = inject(NonNullableFormBuilder).group({
        name: ['', [Validators.required, Validators.maxLength(120)]],
        email: ['', [Validators.email, Validators.maxLength(120)]],
        phone: ['', Validators.maxLength(30)],
    });

    constructor() {
        this.load();
    }

    protected edit(client: Client): void {
        this.editingId.set(client.id);
        this.form.setValue({ name: client.name, email: client.email ?? '', phone: client.phone ?? '' });
    }

    protected cancelEdit(): void {
        this.editingId.set(null);
        this.form.reset();
    }

    protected save(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        const id = this.editingId();
        const request = this.toRequest();
        this.saving.set(true);
        this.error.set(null);

        const operation = id === null ? this.clientApi.create(request) : this.clientApi.update(id, request);
        operation.subscribe({
            next: () => {
                this.saving.set(false);
                this.cancelEdit();
                this.load();
            },
            error: () => {
                this.saving.set(false);
                this.error.set('No se ha podido guardar el cliente');
            },
        });
    }

    protected remove(client: Client): void {
        if (!confirm(`¿Eliminar a ${client.name} y todas sus tareas?`)) {
            return;
        }

        this.clientApi.delete(client.id).subscribe({
            next: () => {
                if (this.editingId() === client.id) {
                    this.cancelEdit();
                }
                this.load();
            },
            error: () => this.error.set('No se ha podido eliminar el cliente'),
        });
    }

    private load(): void {
        this.clientApi.findAll().subscribe({
            next: (clients) => {
                this.clients.set(clients);
                this.loading.set(false);
            },
            error: () => {
                this.loading.set(false);
                this.error.set('No se han podido cargar los clientes');
            },
        });
    }

    private toRequest(): ClientRequest {
        const { name, email, phone } = this.form.getRawValue();

        return { name: name.trim(), email: email.trim() || null, phone: phone.trim() || null };
    }
}
