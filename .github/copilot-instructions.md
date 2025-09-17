Below are focused instructions for AI coding agents to be productive in the Brownford University Portal codebase.

1) Big picture
- Spring Boot 3 web app (Java 17) with server-rendered Thymeleaf UIs for admin/faculty/student.
- MVC layering: controllers in `com.brownford.controller`, business logic in `com.brownford.service`, persistence in `com.brownford.repository`, models in `com.brownford.model`.
- Security: Spring Security is configured in `com.brownford.config.SecurityConfig` (role-based request matchers and BCrypt PasswordEncoder).

2) How to run & build (concrete)
- Dev run (from repo root):
  Set-Location -Path 'D:\CODES\Brownford_University_Portal\brownford'
  .\mvnw.cmd spring-boot:run
- Build (skip tests):
  .\mvnw.cmd -DskipTests=true package
- Database: uses MySQL. Default connection in `src/main/resources/application.properties` uses DATABASE env var fallback. Confirm or override `MYSQL_HOST`, `spring.datasource.username`, `spring.datasource.password` before running.

3) Key patterns and conventions (examples)
- Controllers return Thymeleaf views (e.g. `StudentController.home()` -> `/student/student-home`) and provide REST endpoints under `/api/*` (see `StudentApiController` nested class example).
- Services contain validation/business rules (see `EnrollmentService.createEnrollment` for curriculum checks and chronological term logic). Use service methods for unit-of-work and notifications (NotificationService used after persistence).
- Repository usage: prefer `findById(...).orElseThrow()` in services for failing fast. Some controllers query repositories directly (e.g., `StudentController` looks up the current user via `studentRepository.findAll().stream()...`).
- Security: use `PasswordEncoder` bean from `SecurityConfig` to encode/verify passwords (see `UserManagement`, `StudentController` change-password flow).
- Thymeleaf templates live in `src/main/resources/templates/{admin,faculty,student}` and static JS/CSS in `src/main/resources/static`. UI behaviors (notifications, modals) are in `static/js/*.js` and wire to backend endpoints such as `/api/student/profile-info`.

4) Common tasks for an AI agent
- Add new REST endpoint: put controller under `com.brownford.controller`, use `@RestController` and `/api/...` prefix, call service layer in `com.brownford.service`, and add repository methods in `com.brownford.repository` if needed.
- Modify authentication/roles: update `SecurityConfig.filterChain(...)` requestMatchers and review `CustomAuthenticationSuccessHandler` for role routing.
- Database migrations: project currently uses `spring.jpa.hibernate.ddl-auto=update`. Do not assume migrations — if adding schema changes for production, propose adding Flyway/Liquibase.

5) Testing & debugging notes
- Unit tests: uses Spring Boot test dependency; tests live under `src/test/java` (run with `mvnw.cmd test`).
- Quick debug: run with `spring-boot:run` and attach debugger to port 5005 by adding JVM args in MAVEN_OPTS if needed.
- Logging: `com.brownford` is DEBUG-level by default in `application.properties` — use that to trace service flows.

6) Integration points & externals
- MySQL (runtime dependency `mysql-connector-j`) — ensure DB is available at `spring.datasource.url`.
- No external HTTP APIs appear in the repo; notifications and activity logs are internal services.

7) Files to inspect for context (priority)
- `com.brownford.BrownfordApplication` (entrypoint)
- `com.brownford.config.SecurityConfig` (auth/roles)
- `com.brownford.service.EnrollmentService` (complex business rules)
- `src/main/resources/templates/*` and `static/js/*` (UI wiring)
- `pom.xml` (build and dependency versions)

If anything is unclear or you need more detailed instructions for a specific task (tests, adding endpoints, or changing security), tell me which area to expand and I will update this file.
