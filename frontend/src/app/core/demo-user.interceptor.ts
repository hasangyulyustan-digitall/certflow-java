import { HttpInterceptorFn } from '@angular/common/http';

export const demoUserInterceptor: HttpInterceptorFn = (req, next) => {
  const user = localStorage.getItem('certflow-user');
  let email = localStorage.getItem('certflow-demo-email') ?? '';

  if (user) {
    try {
      email = JSON.parse(user).email;
    } catch {
      localStorage.removeItem('certflow-user');
    }
  }

  if (!email) return next(req);

  return next(req.clone({ setHeaders: { 'X-User-Email': email } }));
};
