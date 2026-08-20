# AI Task Manager — Phase 1 Summary and Phase 2 Plan

This repository contains the AI Task Manager project. This README documents what was completed in Phase 1 (backend foundation), how to run and test the backend locally, known issues, and a proposed, step-by-step plan for Phase 2.

---

## Project overview

AI Task Manager is a simple full-stack application to create and manage tasks, with future capability to have the AI break a large task into subtasks. The project is built incrementally; Phase 1 delivered a production-style Spring Boot backend with a clean layered architecture.

Stack (backend - Phase 1)
- Java 17 (development runtime)
- Spring Boot 3.3.x
- Maven
- Spring Web, Spring Data JPA, Bean Validation
- PostgreSQL (connection via environment variables)
- Flyway for migrations (note: local Flyway/Postgres 17 compatibility issue described below)
- JUnit 5 and Mockito for tests

Files of interest (Phase 1)
- Application entry: [AiTaskManagerApplication](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/AiTaskManagerApplication.java>)
- Main config: [application.yml](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/resources/application.yml>)
- Flyway migration: [V1__create_tasks_table.sql](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/resources/db/migration/V1__create_tasks_table.sql>)
- Task entity: [Task.java](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/entity/Task.java>)
- Service layer: [TaskService.java](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/service/TaskService.java>)
- REST controller: [TaskController.java](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/controller/TaskController.java>)
- AI skeleton service and controller: [AiService.java](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/service/AiService.java>), [AiController.java](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/controller/AiController.java>)
- Flyway programmatic config (temporary): [FlywayConfig.java](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/src/main/java/com/mahi/aitaskmanager/config/FlywayConfig.java>)
- Environment example: [backend/.env.example](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/.env.example>)
- Backend README (more details): [backend/README.md](</Users/maheshkumarparikipandla/Documents/AI Projects/AI Task Manager/backend/README.md>)

---

## What was implemented in Phase 1 (deliverables)

- Project scaffold (Maven + Spring Boot) under `backend/`.
- JPA Entity for Task and TaskStatus/TaskPriority enums.
- DTOs, Mapper, Repository, Service and Controller for Task CRUD.
- Validation (Jakarta Bean Validation) and centralized exception handling with `@RestControllerAdvice`.
- Flyway migration script (V1) to create the `tasks` table.
- AI skeleton service (mock-first) and endpoint: `POST /api/tasks/{id}/ai/breakdown`.
- Unit tests for service layer and controller tests for main endpoints.
- .env.example and .gitignore entries to avoid committing credentials.

Acceptance criteria met for Phase 1:
- All main CRUD endpoints implemented (POST, GET, PUT, PATCH status, DELETE).
- DTOs used for API boundary (no JPA entities exposed directly).
- Constructor injection and layered architecture used.
- Tests added and runnable locally.

---

## How to run the backend locally (development)

1. Prerequisites
- Java 17 installed and available on PATH (the project compiles with `-Dmaven.compiler.release=17`).
- Maven (or use the included Maven wrapper `./mvnw`).
- PostgreSQL (or use a Docker container). NOTE: there is a known Flyway compatibility issue with PostgreSQL 17 (see Known Issues below).

2. Create local env file (do NOT commit)
- Copy `backend/.env.example` to `backend/.env` and fill values:

  cp backend/.env.example backend/.env
  # edit backend/.env to set DB_URL, DB_USERNAME, DB_PASSWORD, AI_API_KEY (optional)

3. Build and run tests
- From the project root:

  ./backend/mvnw -Dmaven.compiler.release=17 clean test

4. Start the backend
- Option A (using your local Postgres and env file):

  # from project root
  cd backend
  source .env   # or export the env vars in your shell
  ./mvnw -Dmaven.compiler.release=17 spring-boot:run

- Option B (if you prefer Docker Postgres 16 — recommended to avoid Flyway/PG17 issues):

  docker run --name aitm-postgres -e POSTGRES_USER=task_user -e POSTGRES_PASSWORD=root -e POSTGRES_DB=task_manager -p 5430:5432 -d postgres:16
  export DB_URL=jdbc:postgresql://localhost:5430/task_manager
  export DB_USERNAME=task_user
  export DB_PASSWORD=root
  cd backend
  ./mvnw -Dmaven.compiler.release=17 spring-boot:run

5. Test the API (examples)
- Create a task:

  curl -X POST http://localhost:8080/api/tasks \
    -H "Content-Type: application/json" \
    -d '{"title":"Test task","description":"Try API","priority":"MEDIUM"}'

- List tasks:

  curl http://localhost:8080/api/tasks

- Get task by id:

  curl http://localhost:8080/api/tasks/1

- Update task:

  curl -X PUT http://localhost:8080/api/tasks/1 -H "Content-Type: application/json" -d '{"title":"Updated","description":"...","priority":"HIGH"}'

- Mark status (PATCH):

  curl -X PATCH http://localhost:8080/api/tasks/1/status -H "Content-Type: application/json" -d '{"status":"COMPLETED"}'

- AI breakdown (mock):

  curl -X POST http://localhost:8080/api/tasks/1/ai/breakdown

---

## Known issues and notes

- Flyway vs PostgreSQL 17: On the developer machine used for initial work, Flyway reported `Unsupported Database: PostgreSQL 17.10`. Temporary approaches used during development:
  - Temporarily disabling Flyway and letting Hibernate `ddl-auto=update` create the schema locally.
  - Programmatic Flyway configuration was tried but the database version was still reported unsupported by Flyway in that environment.
  - Recommended developer workaround: use a local PostgreSQL 16 Docker container for development so Flyway migrations run normally.

- Do NOT commit real secrets into the repository. Use `backend/.env` locally and keep `backend/.env.example` in the repo for placeholders. GitHub Actions secrets should be used for CI.

---

## Phase 2 plan (detailed)

Goal: Extend the backend so tasks can have persistent subtasks, integrate AI breakdown to optionally persist suggested subtasks, and add filtering/pagination and a daily summary endpoint.

Phase 2 will be implemented incrementally in small steps. Each step includes a short acceptance criteria and tests.

STEP A — Subtasks database + entity + migration
- Add Flyway migration V2 to create `subtasks` table with columns:
  - id (PK)
  - task_id (FK -> tasks.id)
  - title
  - description (nullable)
  - status (enum: TODO, IN_PROGRESS, COMPLETED)
  - ord (integer) for ordering
  - created_at, updated_at (timestamps)
- Create `Subtask` JPA entity in `entity/` package
- Acceptance: Migration runs and table exists; entity maps to table (integration verified with Docker Postgres 16).

STEP B — DTOs, mapper, repository
- Add `SubtaskRequest`, `SubtaskResponse`, `SubtaskMapper`
- Add `SubtaskRepository extends JpaRepository<Subtask, Long>`
- Unit tests for the mapper

STEP C — Service layer
- Add `SubtaskService` with methods:
  - createSubtask(taskId, SubtaskRequest)
  - listSubtasks(taskId)
  - updateSubtask(taskId, subtaskId, SubtaskRequest)
  - patchSubtaskStatus(taskId, subtaskId, status)
  - deleteSubtask(taskId, subtaskId)
- Add unit tests (Mockito) covering happy paths and not-found errors

STEP D — Controller and endpoints
- Add `SubtaskController` or extend `TaskController` with endpoints:
  - GET /api/tasks/{taskId}/subtasks
  - POST /api/tasks/{taskId}/subtasks
  - PATCH /api/tasks/{taskId}/subtasks/{subtaskId}
  - PUT /api/tasks/{taskId}/subtasks/{subtaskId}
  - DELETE /api/tasks/{taskId}/subtasks/{subtaskId}
- Add controller tests (@WebMvcTest)

STEP E — AI integration: persist suggestions
- Extend `POST /api/tasks/{id}/ai/breakdown` to accept a query param `persist=true` (or a body flag).
- If `persist=true` and AI (or mock) returns suggestions, create Subtask rows (with sensible defaults).
- Acceptance: calling breakdown with persist creates subtasks associated with the task.

STEP F — Filtering, pagination, search
- Add query parameters to `GET /api/tasks` for:
  - status (TODO, IN_PROGRESS, COMPLETED)
  - priority (LOW, MEDIUM, HIGH)
  - dueBefore / dueAfter
  - page & size (Spring Pageable)
- Update service & repository to use `Specification` or `Query` methods
- Add tests to validate filtering & pagination

STEP G — Daily summary
- Add endpoint: GET /api/summary/daily?date=YYYY-MM-DD (defaults to today)
- Implement a service that computes totals (total tasks, completed, overdue, high-priority due today)
- Optionally add a scheduled job (Spring @Scheduled) to email or persist daily summaries (later)

STEP H — Documentation & API examples
- Update README and backend/README.md with new endpoints, examples, and sample curl commands
- Add OpenAPI/Swagger if desired (optional)

---

## Testing & CI for Phase 2
- Add GitHub Actions workflow to run Maven tests on push/PR (see example in backend/README.md)
- For integration tests that require a real DB, use the `services` feature of GitHub Actions or spin up a Postgres Docker container in the workflow (use postgres:16 image to be safe with Flyway)

Example CI job snippet for integration tests using Postgres 16:

```yaml
services:
  postgres:
    image: postgres:16
    env:
      POSTGRES_DB: task_manager
      POSTGRES_USER: task_user
      POSTGRES_PASSWORD: root
    ports:
      - 5432:5432
```

---

## Rough timeline and estimates (you can adjust)
- STEP A: migration + entity — 1 day
- STEP B: DTOs + mapper + repo — 0.5 day
- STEP C: service + unit tests — 1 day
- STEP D: controller + API tests — 1 day
- STEP E: AI persistence integration — 0.5–1 day
- STEP F: filtering & pagination — 1 day
- STEP G: daily summary — 0.5 day
- Documentation & CI: 0.5 day

Total (Phase 2): ~6–7 days of focused work (can be broken into smaller PRs)

---

## How I’ll proceed if you want me to implement Phase 2 now
I will work in small PR-sized changes. Suggested first PR:
- Add V2 Flyway migration + Subtask entity + repository + mapper + simple service method to list subtasks. Include unit tests for the mapper.

Ask me to start STEP A and I will:
1. Show the specific migration SQL I plan to add.
2. Show the entity code I will add and the exact files to be changed.
3. Run the build & tests in this environment (with the Docker Postgres 16 option if you prefer I run it here).

---

If you want the README adjusted (more/less detail, different formatting, or to include a high-level architecture diagram), say how you want it and I’ll update it. Otherwise I’ll add this file to the repo (or to a new branch) if you ask me to create the commit and push instructions.

