<div align="center">

# Task Manager

### Aplicación web interna para organizar clientes y hacer el seguimiento de sus tareas

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

## Descripción

Aplicación web interna para gestionar los clientes de una empresa y las tareas pendientes de cada uno. Cada tarea tiene un estado y una fecha límite, así el equipo ve de un vistazo qué queda por hacer para cada cliente.

## Funcionalidades

- Inicio de sesión con un usuario interno (JWT).
- Alta, edición y borrado de clientes.
- Tareas por cliente con estado (pendiente, en curso, hecha) y fecha límite.
- Filtro de las tareas de un cliente por estado.
- API REST documentada con Swagger.

## Tecnologías

| Tecnología | Uso |
| --- | --- |
| Java 25 + Spring Boot 4 | API REST |
| Spring Security + JWT | Autenticación |
| Spring Data JPA | Persistencia |
| Supabase (PostgreSQL) | Base de datos |
| springdoc-openapi | Documentación Swagger |
| Angular 21 + TypeScript | Frontend |
| SCSS | Estilos |

## Estructura del proyecto

```
task-manager/
├── backend/                  # API REST con Spring Boot
│   └── src/main/java/.../taskmanager/
│       ├── config/           # Configuración y OpenAPI
│       ├── security/         # Cadena de seguridad y JWT
│       ├── features/         # auth, users, clients, tasks
│       └── web/exception/    # Respuestas de error
└── frontend/                 # Aplicación Angular
    └── src/app/
        ├── core/             # Llamadas a la API, sesión y guards
        ├── shared/models/    # Modelos de cliente y tarea
        ├── layout/           # Layout principal
        └── features/         # login, clients, tasks
```

## Puesta en marcha

### Backend

Copia `backend/src/main/resources/application.properties.example` como `application.properties` en la misma carpeta y rellena la conexión de Supabase, el usuario interno y un secreto JWT de al menos 32 caracteres. Después:

```bash
cd backend
mvn spring-boot:run
```

La API arranca en http://localhost:8080 y Swagger en http://localhost:8080/swagger-ui.html. El usuario interno se crea en el primer arranque.

### Frontend

```bash
cd frontend
npm install
npm start
```

Abre http://localhost:4200. Las peticiones a `/api` se redirigen al backend.

### Tests

```bash
cd backend
mvn test
```

## Endpoints

| Método | Ruta | Descripción |
| --- | --- | --- |
| POST | `/api/v1/auth/login` | Iniciar sesión |
| GET | `/api/v1/clients` | Listar clientes |
| POST | `/api/v1/clients` | Crear un cliente |
| GET | `/api/v1/clients/{id}` | Obtener un cliente |
| PUT | `/api/v1/clients/{id}` | Actualizar un cliente |
| DELETE | `/api/v1/clients/{id}` | Borrar un cliente y sus tareas |
| GET | `/api/v1/clients/{id}/tasks?status=` | Listar las tareas de un cliente |
| POST | `/api/v1/clients/{id}/tasks` | Crear una tarea |
| PUT | `/api/v1/tasks/{id}` | Actualizar una tarea |
| DELETE | `/api/v1/tasks/{id}` | Borrar una tarea |

---

<div align="center">

Made in 2026 by [@adrianpmdev](https://github.com/adrianpmdev)

</div>
