/**
 * AuthSession
 *
 * Signs the user in and keeps the access token for the current browser tab until it expires or the user signs out.
 *
 * @author adrianpmdev
 */

import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { API_BASE_URL } from '../api/api';

interface LoginResponse {
    accessToken: string;
    expiresAt: string;
}

const TOKEN_KEY = 'task-manager.token';
const EXPIRES_AT_KEY = 'task-manager.expires-at';

@Injectable({ providedIn: 'root' })
export class AuthSession {
    private readonly http = inject(HttpClient);
    private readonly router = inject(Router);
    private readonly token = signal<string | null>(this.readStoredToken());

    /** Whether there is a valid, unexpired token. */
    isAuthenticated(): boolean {
        return this.getToken() !== null;
    }

    /** Current token, or null when there is none or it has expired. */
    getToken(): string | null {
        const token = this.token();

        if (token && this.isExpired()) {
            this.clear();
            return null;
        }

        return token;
    }

    /** Signs in and stores the returned token. */
    login(username: string, password: string): Observable<LoginResponse> {
        return this.http
            .post<LoginResponse>(`${API_BASE_URL}/auth/login`, { username, password })
            .pipe(tap((response) => this.store(response)));
    }

    /** Removes the token and goes back to the login screen. */
    logout(): void {
        this.clear();
        void this.router.navigateByUrl('/login');
    }

    private store(response: LoginResponse): void {
        sessionStorage.setItem(TOKEN_KEY, response.accessToken);
        sessionStorage.setItem(EXPIRES_AT_KEY, response.expiresAt);
        this.token.set(response.accessToken);
    }

    private clear(): void {
        sessionStorage.removeItem(TOKEN_KEY);
        sessionStorage.removeItem(EXPIRES_AT_KEY);
        this.token.set(null);
    }

    private isExpired(): boolean {
        const expiresAt = sessionStorage.getItem(EXPIRES_AT_KEY);

        return !expiresAt || Date.parse(expiresAt) <= Date.now();
    }

    private readStoredToken(): string | null {
        return sessionStorage.getItem(TOKEN_KEY);
    }
}
