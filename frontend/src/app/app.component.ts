import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, MatToolbarModule, MatButtonModule],
  template: `
    <mat-toolbar color="primary" *ngIf="auth.user$ | async as user; else guestToolbar">
      <strong>CertFlow</strong>
      <a mat-button routerLink="/">Dashboard</a>
      <a mat-button routerLink="/catalog">Catalog</a>
      <a mat-button routerLink="/my-certifications">My Certifications</a>
      <a mat-button routerLink="/claims">Claims</a>
      <a mat-button routerLink="/approvals">Approvals</a>
      <span class="toolbar-spacer"></span>
      <span class="toolbar-user">{{user.fullName}}</span>
      <button mat-button (click)="logout()">Logout</button>
    </mat-toolbar>
    <ng-template #guestToolbar>
      <mat-toolbar color="primary">
        <strong>CertFlow</strong>
        <span class="toolbar-spacer"></span>
        <a mat-button routerLink="/login">Login</a>
      </mat-toolbar>
    </ng-template>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  constructor(public auth: AuthService, private router: Router) {}

  logout() {
    this.auth.logout();
    this.router.navigateByUrl('/login');
  }
}
