import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { interval } from 'rxjs';
import { ApiService } from '../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, MatCardModule],
  template: `
  <div class="page">
    <h1>Dashboard</h1>
    <div class="grid" *ngIf="summary">
      <div class="card"><h3>Hello, {{summary.fullName}}</h3><p>Role: <b>{{summary.role}}</b></p></div>
      <div class="card"><h3>{{summary.planned}}</h3><p>Planned / In progress</p></div>
      <div class="card"><h3>{{summary.passed}}</h3><p>Passed certifications</p></div>
      <div class="card"><h3>{{summary.pendingClaims}}</h3><p>Pending claims</p></div>
      <div class="card"><h3>{{summary.paidAmount | currency:'EUR'}}</h3><p>Paid reimbursements</p></div>
    </div>
  </div>`
})
export class DashboardComponent implements OnInit {
  summary: any;
  constructor(private api: ApiService) {}
  ngOnInit() {
    this.load();
    interval(15000).subscribe(() => this.load());
  }

  private load() {
    this.api.dashboard().subscribe(x => this.summary = x);
  }
}
