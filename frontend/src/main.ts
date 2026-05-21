import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Routes } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppComponent } from './app/app.component';
import { DashboardComponent } from './app/features/dashboard.component';
import { CatalogComponent } from './app/features/catalog.component';
import { MyCertificationsComponent } from './app/features/my-certifications.component';
import { ClaimsComponent } from './app/features/claims.component';
import { ApprovalsComponent } from './app/features/approvals.component';
import { AuthComponent } from './app/features/auth.component';
import { demoUserInterceptor } from './app/core/demo-user.interceptor';
import { authGuard } from './app/core/auth.guard';

const routes: Routes = [
  { path: 'login', component: AuthComponent },
  { path: '', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'catalog', component: CatalogComponent, canActivate: [authGuard] },
  { path: 'my-certifications', component: MyCertificationsComponent, canActivate: [authGuard] },
  { path: 'claims', component: ClaimsComponent, canActivate: [authGuard] },
  { path: 'approvals', component: ApprovalsComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes), provideHttpClient(withInterceptors([demoUserInterceptor])), provideAnimations()]
}).catch(err => console.error(err));
