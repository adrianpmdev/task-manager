# Task Manager

Internal web application to organize clients and track their tasks by status and due date.

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F)
![Angular](https://img.shields.io/badge/Angular-21-DD0031)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3ECF8E)

## Features

- Sign in with an internal user (JWT)
- Create, edit and delete clients
- Tasks per client with status (pending, in progress, done) and due date
- Filter tasks by status
- API documented with Swagger UI

## Structure

```
backend/    Spring Boot REST API
frontend/   Angular application
```

## Running locally

### Backend

1. Copy `backend/src/main/resources/application.properties.example` to `application.properties` in the same folder and fill in the Supabase (PostgreSQL) connection, the internal user and a JWT secret of at least 32 characters.
2. Start it:

```bash
cd backend
mvn spring-boot:run
```

The API runs on `http://localhost:8080` and Swagger UI on `http://localhost:8080/swagger-ui.html`.
The internal user is created on the first start.

### Frontend

```bash
cd frontend
npm install
npm start
```

Open `http://localhost:4200`. Requests to `/api` are forwarded to the backend.

### Tests

```bash
cd backend
mvn test
```

## API

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/v1/auth/login` | Sign in |
| GET / POST | `/api/v1/clients` | List / create clients |
| GET / PUT / DELETE | `/api/v1/clients/{id}` | Get / update / delete a client |
| GET / POST | `/api/v1/clients/{id}/tasks?status=` | List / create tasks of a client |
| PUT / DELETE | `/api/v1/tasks/{id}` | Update / delete a task |

---

![Author](https://img.shields.io/badge/Author-adrianpmdev-181717?logo=github)

Made in 2026 by [@adrianpmdev](https://github.com/adrianpmdev)
