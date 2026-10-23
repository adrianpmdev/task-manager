/**
 * Login
 *
 * Sign in screen.
 *
 * @author adrianpmdev
 */

import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthSession } from '@core/auth/auth-session';

const UNAUTHORIZED = 401;

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-login',
    imports: [ReactiveFormsModule],
    templateUrl: './login.html',
    styleUrl: './login.scss',
})
export class Login {
    private readonly authSession = inject(AuthSession);
    private readonly router = inject(Router);

    protected readonly form = inject(NonNullableFormBuilder).group({
        username: ['', Validators.required],
        password: ['', Validators.required],
    });
    protected readonly submitting = signal(false);
    protected readonly error = signal<string | null>(null);

    protected submit(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        const { username, password } = this.form.getRawValue();
        this.submitting.set(true);
        this.error.set(null);

        this.authSession.login(username, password).subscribe({
            next: () => void this.router.navigateByUrl('/clients'),
            error: (error: unknown) => {
                this.submitting.set(false);
                this.error.set(
                    error instanceof HttpErrorResponse && error.status === UNAUTHORIZED
                        ? 'Usuario o contraseña incorrectos'
                        : 'No se ha podido iniciar sesión',
                );
            },
        });
    }
}
