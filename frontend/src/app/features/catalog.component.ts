import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { ApiService, Certificate } from '../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, MatButtonModule],
  template: `
  <div class="page">
    <h1>Certificate Catalog</h1>
    <table class="table">
      <thead><tr><th>Vendor</th><th>Name</th><th>Level</th><th>Exam</th><th>Cost</th><th>Bonus</th><th></th></tr></thead>
      <tbody>
        <tr *ngFor="let c of certificates">
          <td>{{c.vendor}}</td><td>{{c.name}}</td><td>{{c.level}}</td><td>{{c.examCode}}</td>
          <td>{{c.defaultExamCost | currency:c.currency}}</td><td>{{c.bonusAmount | currency:c.currency}}</td>
          <td><button mat-raised-button color="primary" (click)="assign(c)">Assign to me</button></td>
        </tr>
      </tbody>
    </table>
  </div>`
})
export class CatalogComponent implements OnInit {
  certificates: Certificate[] = [];
  constructor(private api: ApiService) {}
  ngOnInit() { this.api.certificates().subscribe(x => this.certificates = x); }
  assign(c: Certificate) { this.api.assignCertificate(c.id).subscribe(() => alert(`${c.name} assigned.`)); }
}
