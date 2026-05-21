# CertFlow Java Stack

Standalone Java/Spring Boot version of CertFlow with its own frontend copy and Docker Compose setup.

This directory is isolated from the existing .NET project.

## Stack

- Backend: Java 21, Spring Boot, Spring Data JPA, PostgreSQL
- Frontend: copied Angular UI, configured for Java API on `localhost:8081`
- Runtime: Docker Compose with PostgreSQL, API, and nginx-served frontend

## Run

```bash
cd java-certflow
docker compose up --build
```

Open:

```text
http://localhost:4300
```

Java API:

```text
http://localhost:8081/api
```

PostgreSQL is exposed on local port `55432` to avoid colliding with the original project.

## Seed Users

All seed users use password `password1`.

| Role | Email |
|---|---|
| Employee | employee@company.local |
| Manager | manager@company.local |
| HR | hr@company.local |
| Finance | finance@company.local |
| Admin | admin@company.local |

## Implemented API

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/dashboard/summary`
- `GET /api/certificates`
- `GET /api/my-certifications`
- `POST /api/my-certifications`
- `GET /api/claims/mine`
- `GET /api/claims/pending-approvals`
- `POST /api/claims`
- `POST /api/claims/{id}/approve`
- `POST /api/claims/{id}/paid`
