/**
 * authInterceptor
 *
 * Adds the Bearer token to API requests. When the API answers 401 outside the login the session is closed,
 * because the token is no longer valid.
 *
 * @author adrianpmdev
 */

import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { API_BASE_URL } from '../api/api';
import { AuthSession } from './auth-session';

const UNAUTHORIZED = 401;

export const authInterceptor: HttpInterceptorFn = (request, next) => {
    const authSession = inject(AuthSession);
    const token = authSession.getToken();
    const authenticatedRequest = token
        ? request.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
        : request;

    if (request.url.startsWith(`${API_BASE_URL}/auth/`)) {
        return next(authenticatedRequest);
    }

    return next(authenticatedRequest).pipe(
        catchError((error: unknown) => {
            if (error instanceof HttpErrorResponse && error.status === UNAUTHORIZED) {
                authSession.logout();
            }

            return throwError(() => error);
        }),
    );
};
