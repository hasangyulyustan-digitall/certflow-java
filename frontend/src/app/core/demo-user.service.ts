import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class DemoUserService {
  get email(): string { return localStorage.getItem('certflow-demo-email') ?? 'employee@company.local'; }
  set email(value: string) { localStorage.setItem('certflow-demo-email', value); }
}
