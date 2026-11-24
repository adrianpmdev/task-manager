<div align="center">

# Task Manager

### Internal web application to organize clients and track their tasks

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5.9-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![SCSS](https://img.shields.io/badge/SCSS-CC6699?style=for-the-badge&logo=sass&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3FCF8E?style=for-the-badge&logo=supabase&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
[![Author](https://img.shields.io/badge/Author-adrianpmdev-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/adrianpmdev)

</div>

---

## Description

Internal web application to keep the clients of a company and the tasks pending for each of them. Every task has a status and a due date, so the team can see at a glance what is left to do for each client.

## Features

- Sign in with an internal user (JWT).
- Create, edit and delete clients.
- Tasks per client with status (pending, in progress, done) and due date.
- Filter the tasks of a client by status.
- REST API documented with Swagger.

## Tech stack

| Technology | Purpose |
| --- | --- |
| Java 25 + Spring Boot 4 | REST API |
| Spring Security + JWT | Authentication |
| Spring Data JPA | Persistence |
| Supabase (PostgreSQL) | Database |
| springdoc-openapi | Swagger documentation |
| Angular 21 + TypeScript | Frontend |
| SCSS | Styles |

## Project structure

```
task-manager/
├── backend/                  # Spring Boot REST API
│   └── src/main/java/.../taskmanager/
│       ├── config/           # Settings and OpenAPI
│       ├── security/         # Security chain and JWT
│       ├── features/         # auth, users, clients, tasks
│       └── web/exception/    # Error responses
└── frontend/                 # Angular application
    └── src/app/
        ├── core/             # API calls, session, guards
        ├── shared/models/    # Client and task models
        ├── layout/           # Main layout
        └── features/         # login, clients, tasks
```

## Getting started

### Backend

Copy `backend/src/main/resources/application.properties.example` to `application.properties` in the same folder and fill in the Supabase connection, the internal user and a JWT secret of at least 32 characters. Then:

```bash
cd backend
mvn spring-boot:run
```

The API runs on http://localhost:8080 and Swagger on http://localhost:8080/swagger-ui.html. The internal user is created on first start.

### Frontend

```bash
cd frontend
npm install
npm start
```

Open http://localhost:4200. Requests to `/api` are forwarded to the backend.

### Tests

```bash
cd backend
mvn test
```

## Endpoints

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/v1/auth/login` | Sign in |
| GET | `/api/v1/clients` | List clients |
| POST | `/api/v1/clients` | Create a client |
| GET | `/api/v1/clients/{id}` | Get a client |
| PUT | `/api/v1/clients/{id}` | Update a client |
| DELETE | `/api/v1/clients/{id}` | Delete a client and its tasks |
| GET | `/api/v1/clients/{id}/tasks?status=` | List the tasks of a client |
| POST | `/api/v1/clients/{id}/tasks` | Create a task |
| PUT | `/api/v1/tasks/{id}` | Update a task |
| DELETE | `/api/v1/tasks/{id}` | Delete a task |

---

<div align="center">

Made in 2026 by [@adrianpmdev](https://github.com/adrianpmdev)

</div>
