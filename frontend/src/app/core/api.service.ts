import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Certificate { id: string; name: string; vendor: string; category: string; level: string; examCode?: string; defaultExamCost: number; currency: string; bonusAmount: number; validityMonths?: number; }
export interface EmployeeCertification { id: string; certificate: Certificate; status: string; targetDate?: string; completedAt?: string; expiryDate?: string; }
export interface Claim { id: string; amount: number; currency: string; claimType: string; status: string; paymentDate: string; description?: string; employeeCertification: EmployeeCertification; }
export interface AuthUser { id: string; email: string; fullName: string; department: string; role: string; }

@Injectable({ providedIn: 'root' })
export class ApiService {
  // Runtime Docker deployments still depend on the host exposing the API on this port.
  private baseUrl = 'http://localhost:8081/api';
  constructor(private http: HttpClient) {}
  login(email: string, password: string) { return this.http.post<AuthUser>(`${this.baseUrl}/auth/login`, { email, password }); }
  register(payload: { email: string; password: string; fullName: string; department?: string }) { return this.http.post<AuthUser>(`${this.baseUrl}/auth/register`, payload); }
  dashboard() { return this.http.get<any>(`${this.baseUrl}/dashboard/summary`); }
  certificates() { return this.http.get<Certificate[]>(`${this.baseUrl}/certificates`); }
  assignCertificate(certificateId: string, targetDate?: string) { return this.http.post(`${this.baseUrl}/my-certifications`, { certificateId, targetDate }); }
  myCertifications() { return this.http.get<EmployeeCertification[]>(`${this.baseUrl}/my-certifications`); }
  createClaim(payload: any) { return this.http.post(`${this.baseUrl}/claims`, payload); }
  myClaims() { return this.http.get<Claim[]>(`${this.baseUrl}/claims/mine`); }
  pendingApprovals() { return this.http.get<Claim[]>(`${this.baseUrl}/claims/pending-approvals`); }
  decideClaim(id: string, decision: 'Approved'|'Rejected'|'Returned', comment?: string) { return this.http.post(`${this.baseUrl}/claims/${id}/approve`, { decision, comment }); }
  markPaid(id: string) { return this.http.post(`${this.baseUrl}/claims/${id}/paid`, {}); }
}
