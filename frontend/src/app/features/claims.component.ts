import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ApiService, Claim, EmployeeCertification } from '../core/api.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatInputModule, MatSelectModule],
  template: `
  <div class="page">
    <h1>Submit Claim</h1>
    <div class="card">
      <div class="form-row">
        <mat-form-field><mat-label>Certification</mat-label><mat-select [(ngModel)]="form.employeeCertificationId"><mat-option *ngFor="let c of certs" [value]="c.id">{{c.certificate.name}}</mat-option></mat-select></mat-form-field>
        <mat-form-field><mat-label>Type</mat-label><mat-select [(ngModel)]="form.claimType"><mat-option value="ExamFee">Exam Fee</mat-option><mat-option value="TrainingCourse">Training Course</mat-option><mat-option value="LearningMaterial">Learning Material</mat-option><mat-option value="RenewalFee">Renewal Fee</mat-option></mat-select></mat-form-field>
        <mat-form-field><mat-label>Amount</mat-label><input matInput type="number" [(ngModel)]="form.amount"></mat-form-field>
        <mat-form-field><mat-label>Currency</mat-label><input matInput [(ngModel)]="form.currency"></mat-form-field>
        <mat-form-field><mat-label>Payment date</mat-label><input matInput type="date" [(ngModel)]="form.paymentDate"></mat-form-field>
      </div>
      <mat-form-field style="width:100%"><mat-label>Description</mat-label><input matInput [(ngModel)]="form.description"></mat-form-field>
      <button mat-raised-button color="primary" (click)="submit()">Submit claim</button>
    </div>

    <h2>My Claims</h2>
    <table class="table">
      <thead><tr><th>Certificate</th><th>Type</th><th>Amount</th><th>Status</th><th>Payment date</th></tr></thead>
      <tbody><tr *ngFor="let c of claims"><td>{{c.employeeCertification.certificate.name}}</td><td>{{c.claimType}}</td><td>{{c.amount | currency:c.currency}}</td><td><span class="status">{{c.status}}</span></td><td>{{c.paymentDate}}</td></tr></tbody>
    </table>
  </div>`
})
export class ClaimsComponent implements OnInit {
  certs: EmployeeCertification[] = [];
  claims: Claim[] = [];
  form = { employeeCertificationId: '', claimType: 'ExamFee', amount: 0, currency: 'EUR', paymentDate: new Date().toISOString().substring(0,10), description: '' };
  constructor(private api: ApiService) {}
  ngOnInit() { this.reload(); }
  reload() { this.api.myCertifications().subscribe(x => this.certs = x); this.api.myClaims().subscribe(x => this.claims = x); }
  submit() { this.api.createClaim(this.form).subscribe(() => this.reload()); }
}
