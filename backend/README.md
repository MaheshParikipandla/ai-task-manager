# AI Task Manager Backend

This backend is a Spring Boot application for the AI Task Manager project.

## Requirements

- Java 21
- Maven
- PostgreSQL

## Setup

1. Copy `.env.example` to `.env` and fill in your PostgreSQL credentials.
2. Create the PostgreSQL database referenced by `DB_URL`.
3. Configure the database credentials in `.env` or your shell environment.

Example:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/aitaskmanager
export DB_USERNAME=postgres
export DB_PASSWORD=password
```

## Run

Use the Maven wrapper included in this project to build and start the application:

```bash
./mvnw clean package
./mvnw spring-boot:run
```

The API listens at `http://localhost:8080`.

If your local JDK is not Java 21, install Java 21 or use a compatible JDK before running the application.

## Endpoints

- `POST /api/tasks`
- `GET /api/tasks`
- `GET /api/tasks/{id}`
- `PUT /api/tasks/{id}`
- `PATCH /api/tasks/{id}/status`
- `DELETE /api/tasks/{id}`

## Testing

```bash
mvn test
```
