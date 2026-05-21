import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ApiService, AuthUser } from './api.service';

const storageKey = 'certflow-user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly userSubject = new BehaviorSubject<AuthUser | null>(this.loadUser());
  readonly user$ = this.userSubject.asObservable();

  constructor(private api: ApiService) {}

  get user(): AuthUser | null {
    return this.userSubject.value;
  }

  login(email: string, password: string) {
    return this.api.login(email, password);
  }

  register(payload: { email: string; password: string; fullName: string; department?: string }) {
    return this.api.register(payload);
  }

  setUser(user: AuthUser) {
    localStorage.setItem(storageKey, JSON.stringify(user));
    localStorage.setItem('certflow-demo-email', user.email);
    this.userSubject.next(user);
  }

  logout() {
    localStorage.removeItem(storageKey);
    localStorage.removeItem('certflow-demo-email');
    this.userSubject.next(null);
  }

  private loadUser(): AuthUser | null {
    const value = localStorage.getItem(storageKey);
    if (!value) return null;

    try {
      return JSON.parse(value) as AuthUser;
    } catch {
      localStorage.removeItem(storageKey);
      return null;
    }
  }
}
