/**
 * routes
 *
 * Routes of the application. Everything except the login requires a signed in user.
 *
 * @author adrianpmdev
 */

import { Routes } from '@angular/router';
import { authGuard } from '@core/auth/auth-guard';
import { guestGuard } from '@core/auth/guest-guard';
import { MainLayout } from '@layout/main-layout/main-layout';

export const routes: Routes = [
    {
        path: 'login',
        canActivate: [guestGuard],
        loadComponent: () => import('@features/login/login').then((m) => m.Login),
    },
    {
        path: '',
        component: MainLayout,
        canActivate: [authGuard],
        children: [],
    },
    { path: '**', redirectTo: '' },
];
