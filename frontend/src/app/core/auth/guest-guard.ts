/**
 * guestGuard
 *
 * Keeps signed in users away from the login screen.
 *
 * @author adrianpmdev
 */

import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthSession } from './auth-session';

export const guestGuard: CanActivateFn = () =>
    !inject(AuthSession).isAuthenticated() || inject(Router).parseUrl('/clients');
