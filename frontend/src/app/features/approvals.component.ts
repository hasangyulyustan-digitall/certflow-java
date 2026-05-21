import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { ApiService, Claim } from '../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  template: `
  <div class="page">
    <h1>Pending Approvals</h1>
    <table class="table">
      <thead><tr><th>Certificate</th><th>Type</th><th>Amount</th><th>Status</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let c of claims">
          <td>{{c.employeeCertification.certificate.name}}</td><td>{{c.claimType}}</td><td>{{c.amount | currency:c.currency}}</td><td><span class="status">{{c.status}}</span></td>
          <td><button mat-button color="primary" (click)="approve(c)">Approve</button><button mat-button color="warn" (click)="reject(c)">Reject</button><button mat-button (click)="paid(c)">Mark paid</button></td>
        </tr>
      </tbody>
    </table>
  </div>`
})
export class ApprovalsComponent implements OnInit {
  claims: Claim[] = [];
  constructor(private api: ApiService) {}
  ngOnInit() { this.reload(); }
  reload() { this.api.pendingApprovals().subscribe(x => this.claims = x); }
  approve(c: Claim) { this.api.decideClaim(c.id, 'Approved', 'Approved from portal').subscribe(() => this.reload()); }
  reject(c: Claim) { this.api.decideClaim(c.id, 'Rejected', 'Rejected from portal').subscribe(() => this.reload()); }
  paid(c: Claim) { this.api.markPaid(c.id).subscribe(() => this.reload()); }
}
