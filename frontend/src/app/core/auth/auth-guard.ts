/**
 * authGuard
 *
 * Only lets signed in users through; the rest are sent to the login screen.
 *
 * @author adrianpmdev
 */

import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthSession } from './auth-session';

export const authGuard: CanActivateFn = () =>
    inject(AuthSession).isAuthenticated() || inject(Router).parseUrl('/login');
