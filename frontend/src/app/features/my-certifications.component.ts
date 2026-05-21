import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, EmployeeCertification } from '../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="page">
    <h1>My Certifications</h1>
    <table class="table">
      <thead><tr><th>Certificate</th><th>Vendor</th><th>Status</th><th>Target</th><th>Completed</th><th>Expiry</th></tr></thead>
      <tbody>
        <tr *ngFor="let item of items">
          <td>{{item.certificate.name}}</td><td>{{item.certificate.vendor}}</td><td><span class="status">{{item.status}}</span></td>
          <td>{{item.targetDate || '-'}}</td><td>{{item.completedAt || '-'}}</td><td>{{item.expiryDate || '-'}}</td>
        </tr>
      </tbody>
    </table>
  </div>`
})
export class MyCertificationsComponent implements OnInit {
  items: EmployeeCertification[] = [];
  constructor(private api: ApiService) {}
  ngOnInit() { this.api.myCertifications().subscribe(x => this.items = x); }
}
