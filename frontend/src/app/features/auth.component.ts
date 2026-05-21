import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { AuthService } from '../core/auth.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatTabsModule],
  template: `
  <div class="auth-page">
    <mat-card class="auth-card">
      <mat-card-title>CertFlow</mat-card-title>
      <mat-tab-group>
        <mat-tab label="Login">
          <form class="auth-form" (ngSubmit)="login()">
            <mat-form-field appearance="outline">
              <mat-label>Email</mat-label>
              <input matInput type="email" name="loginEmail" [(ngModel)]="loginEmail" required>
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Password</mat-label>
              <input matInput type="password" name="loginPassword" [(ngModel)]="loginPassword" required>
            </mat-form-field>
            <button mat-raised-button color="primary" type="submit">Login</button>
          </form>
        </mat-tab>
        <mat-tab label="Register">
          <form class="auth-form" (ngSubmit)="register()">
            <mat-form-field appearance="outline">
              <mat-label>Full name</mat-label>
              <input matInput name="fullName" [(ngModel)]="fullName" required>
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Email</mat-label>
              <input matInput type="email" name="registerEmail" [(ngModel)]="registerEmail" required>
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Department</mat-label>
              <input matInput name="department" [(ngModel)]="department">
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Password</mat-label>
              <input matInput type="password" name="registerPassword" [(ngModel)]="registerPassword" required minlength="7">
            </mat-form-field>
            <button mat-raised-button color="primary" type="submit">Register</button>
          </form>
        </mat-tab>
      </mat-tab-group>
      <p class="auth-error" *ngIf="error">{{error}}</p>
    </mat-card>
  </div>`
})
export class AuthComponent {
  loginEmail = '';
  loginPassword = '';
  fullName = '';
  registerEmail = '';
  department = '';
  registerPassword = '';
  error = '';

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.error = '';
    this.auth.login(this.loginEmail, this.loginPassword).subscribe({
      next: user => this.finish(user),
      error: err => this.error = err.error || 'Login failed.'
    });
  }

  register() {
    this.error = '';
    if (this.registerPassword.length <= 6) {
      this.error = 'Password must be longer than 6 characters.';
      return;
    }

    this.auth.register({
      email: this.registerEmail,
      password: this.registerPassword,
      fullName: this.fullName,
      department: this.department
    }).subscribe({
      next: user => this.finish(user),
      error: err => this.error = err.error || 'Registration failed.'
    });
  }

  private finish(user: any) {
    this.auth.setUser(user);
    this.router.navigateByUrl('/');
  }
}
