/**
 * ClientApi
 *
 * Calls to the client endpoints of the API.
 *
 * @author adrianpmdev
 */

import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Client, ClientRequest } from '@shared/models/client';
import { API_BASE_URL } from '../api/api';

const CLIENTS_URL = `${API_BASE_URL}/clients`;

@Injectable({ providedIn: 'root' })
export class ClientApi {
    private readonly http = inject(HttpClient);

    /** Lists every client sorted by name. */
    findAll(): Observable<Client[]> {
        return this.http.get<Client[]>(CLIENTS_URL);
    }

    /** Gets a client. */
    findById(id: number): Observable<Client> {
        return this.http.get<Client>(`${CLIENTS_URL}/${id}`);
    }

    /** Creates a client. */
    create(request: ClientRequest): Observable<Client> {
        return this.http.post<Client>(CLIENTS_URL, request);
    }

    /** Updates a client. */
    update(id: number, request: ClientRequest): Observable<Client> {
        return this.http.put<Client>(`${CLIENTS_URL}/${id}`, request);
    }

    /** Deletes a client and its tasks. */
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${CLIENTS_URL}/${id}`);
    }
}
