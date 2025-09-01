**Brownford** University Portal
==========================

One-page summary

What this project is
--------------------
A Spring Boot + Thymeleaf web portal for university operations with role-based UIs for Students, Faculty, and Admin. It uses Spring Data JPA repositories, service-layer business logic, and server-rendered Thymeleaf templates with client-side JavaScript for interactive parts (notifications, modals, etc.).

Core features (by role)
----------------------
Admin
- Dashboard and activity log
- Manage students and view student records
- Manage faculty and faculty workload
- Manage programs, curricula, courses, and sections
- Assign schedules to sections/faculty
- Review and approve enrollments
Files: `src/main/resources/templates/admin/*`, `com.brownford.controller.AdminController`, `ProgramManagement`, `CurriculumController`, `CourseController`, `SectionController`, `ScheduleController`, `ActivityLogController`

Faculty
- Faculty dashboard and profile
- View schedule, class lists, and workload
- Grading interface (grading sheet)
Files: `src/main/resources/templates/faculty/*`, `FacultyController`, `FacultyAssignmentController`, `GradeController`, `FacultyAssignmentService`, `GradeService`

Student
- Student dashboard and profile
- Enrollment request flow with validation
- View schedule and grades
Files: `src/main/resources/templates/student/*`, `StudentController`, `EnrollmentController`, `StudentScheduleApiController`, `StudentGradesApiController`, `EnrollmentService`

Notifications
- Create and manage notifications for Students, Faculty, and Admin
- Admin-targeted notifications support `targetUrl` for deep links
Files: `com.brownford.service.NotificationService`, `NotificationController`, `src/main/resources/static/js/notifications.js`

Data & Persistence
------------------
- JPA models and repositories for: User, Student, Faculty, Enrollment, Course, Section, Schedule, Program, Curriculum, Grade, Notification, ActivityLog
Files: `com.brownford.repository.*`

How to run (dev)
-----------------
From the project root (this repo contains a Maven wrapper):

```powershell
Set-Location -Path 'D:\Brownford_University_Portal\brownford'
.\mvnw.cmd spring-boot:run
```

Build without running tests:

```powershell
Set-Location -Path 'D:\Brownford_University_Portal\brownford'
.\mvnw.cmd -DskipTests=true package
```

Important files to inspect
--------------------------
- `com.brownford.BrownfordApplication` — Spring Boot entrypoint
- `src/main/resources/templates/` — Thymeleaf UI templates (admin, faculty, student)
- `com.brownford.controller.*` — HTTP endpoints and controllers
- `com.brownford.service.*` — Business logic
- `com.brownford.repository.*` — JPA repositories
- `src/main/resources/static/js/` — Client-side JavaScript
- `src/main/resources/application.properties` — Application configuration

Next steps you might want
-------------------------
- Replace `null` targetUrl values with meaningful deep links (e.g., `/admin/enrollments`).
- Remove deprecated overloads in `NotificationService` after ensuring no external callers remain.
- Add an automated test suite run in CI and a CONTRIBUTING guide.

Contact
-------
For code questions, open the controller/service file and refer to the files listed above.
