/**
 * MainLayout
 *
 * Frame of the private screens: header with the sign out button and the routed content.
 *
 * @author adrianpmdev
 */

import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthSession } from '@core/auth/auth-session';

@Component({
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-main-layout',
    imports: [RouterLink, RouterOutlet],
    templateUrl: './main-layout.html',
    styleUrl: './main-layout.scss',
})
export class MainLayout {
    private readonly authSession = inject(AuthSession);

    protected logout(): void {
        this.authSession.logout();
    }
}
