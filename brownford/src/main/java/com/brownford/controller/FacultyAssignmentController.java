package com.brownford.controller;

import com.brownford.dto.FacultyAssignmentWithCourseDTO;
import com.brownford.dto.FacultyAssignmentWithScheduleDTO;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Course;
import com.brownford.model.Faculty;
import com.brownford.model.FacultyAssignment;
import com.brownford.service.FacultyAssignmentService;
import com.brownford.repository.ScheduleRepository;
import com.brownford.model.Schedule;
import com.brownford.service.ActivityLogService;
import com.brownford.exception.ScheduleConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/faculty-assignments")
public class FacultyAssignmentController {
    @Autowired
    private FacultyAssignmentService facultyAssignmentService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping
    public List<FacultyAssignmentWithCourseDTO> getAllFacultyAssignments(
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long curriculumCourseId,
            @RequestParam(required = false) String semester) {
        List<FacultyAssignment> all = facultyAssignmentService.getAllFacultyAssignments();
        if (sectionId != null) {
            all = all.stream().filter(fa -> fa.getSection().getId().equals(sectionId)).toList();
        }
        if (curriculumCourseId != null) {
            all = all.stream().filter(fa -> fa.getCurriculumCourse().getId().equals(curriculumCourseId)).toList();
        }
        if (semester != null) {
            all = all.stream().filter(fa -> fa.getSemester().equalsIgnoreCase(semester)).toList();
        }
        // Map to DTOs
        return all.stream().map(fa -> {
            FacultyAssignmentWithCourseDTO dto = new FacultyAssignmentWithCourseDTO();
            dto.setAssignmentId(fa.getId());
            dto.setSectionId(fa.getSection() != null ? fa.getSection().getId() : null);
            CurriculumCourse cc = fa.getCurriculumCourse();
            dto.setCurriculumCourseId(cc != null ? cc.getId() : null);
            dto.setSemester(fa.getSemester());
            dto.setYearLevel(fa.getYearLevel());
            dto.setFacultyId(fa.getFaculty() != null ? fa.getFaculty().getId() : null);
            Faculty faculty = fa.getFaculty();
            if (faculty != null && faculty.getUser() != null) {
                String facultyName = faculty.getUser().getFirstName() + " " + faculty.getUser().getLastName();
                dto.setFacultyName(facultyName);
                dto.setFaculty(facultyName);
            } else {
                dto.setFacultyName(null);
                dto.setFaculty(null);
            }
            // CurriculumCourse details
            if (cc != null) {
                Course course = cc.getCourse();
                dto.setCourseCode(course != null ? course.getCourseCode() : null);
                dto.setCourseTitle(course != null ? course.getCourseTitle() : null);
                dto.setCourseYearLevel(cc.getYearLevel());
                dto.setCourseSemester(cc.getSemester());
            }
            // Fetch and set schedule fields
            if (cc != null && fa.getSection() != null) {
                List<Schedule> schedules = scheduleRepository.findByCurriculumCourseAndSection(cc, fa.getSection());
                if (!schedules.isEmpty()) {
                    Schedule sched = schedules.get(0); // Assume one schedule per course-section
                    dto.setDay(sched.getDay());
                    dto.setStartTime(sched.getStartTime() != null ? sched.getStartTime().toString() : null);
                    dto.setEndTime(sched.getEndTime() != null ? sched.getEndTime().toString() : null);
                    dto.setRoom(sched.getRoom());
                }
            }
            dto.setSchoolYear(fa.getSchoolYear());
            return dto;
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyAssignment> getFacultyAssignmentById(@PathVariable Long id) {
        Optional<FacultyAssignment> assignment = facultyAssignmentService.getFacultyAssignmentById(id);
        return assignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FacultyAssignment createFacultyAssignment(@RequestBody FacultyAssignment assignment, Principal principal) {
        FacultyAssignment saved = facultyAssignmentService.saveFacultyAssignment(assignment);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Assigned faculty (ID: " + (assignment.getFaculty() != null ? assignment.getFaculty().getId() : "None") + ") to section (ID: " + (assignment.getSection() != null ? assignment.getSection().getId() : "None") + ") for course (ID: " + (assignment.getCurriculumCourse() != null ? assignment.getCurriculumCourse().getId() : "None") + ")";
        activityLogService.log(adminUsername, "Assigned Faculty to Section", details);
        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyAssignment> updateFacultyAssignment(@PathVariable Long id,
            @RequestBody FacultyAssignment updated, Principal principal) {
        Optional<FacultyAssignment> assignmentOpt = facultyAssignmentService.getFacultyAssignmentById(id);
        if (assignmentOpt.isEmpty())
            return ResponseEntity.notFound().build();
        FacultyAssignment assignment = assignmentOpt.get();
        assignment.setCurriculumCourse(updated.getCurriculumCourse());
        assignment.setSection(updated.getSection());
        assignment.setFaculty(updated.getFaculty());
        assignment.setSemester(updated.getSemester());
        assignment.setYearLevel(updated.getYearLevel());
        FacultyAssignment saved = facultyAssignmentService.saveFacultyAssignment(assignment);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Updated faculty assignment (ID: " + id + ") - Faculty (ID: " + (updated.getFaculty() != null ? updated.getFaculty().getId() : "None") + "), Section (ID: " + (updated.getSection() != null ? updated.getSection().getId() : "None") + "), Course (ID: " + (updated.getCurriculumCourse() != null ? updated.getCurriculumCourse().getId() : "None") + ")";
        activityLogService.log(adminUsername, "Updated Faculty Assignment", details);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacultyAssignment(@PathVariable Long id, Principal principal) {
        facultyAssignmentService.deleteFacultyAssignment(id);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Deleted faculty assignment with ID: " + id;
        activityLogService.log(adminUsername, "Deleted Faculty Assignment", details);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/with-schedule")
    public ResponseEntity<FacultyAssignment> createFacultyAssignmentWithSchedule(
            @RequestBody FacultyAssignmentWithScheduleDTO dto, Principal principal) {
        FacultyAssignment saved = facultyAssignmentService.saveFacultyAssignmentWithSchedule(dto);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Assigned faculty (ID: " + (dto.getFacultyId() != null ? dto.getFacultyId() : "None") + ") to section (ID: " + (dto.getSectionId() != null ? dto.getSectionId() : "None") + ") for course (ID: " + (dto.getCurriculumCourseId() != null ? dto.getCurriculumCourseId() : "None") + ") with schedule.";
        activityLogService.log(adminUsername, "Assigned Faculty to Section with Schedule", details);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/with-schedule/{id}")
    public ResponseEntity<FacultyAssignment> updateFacultyAssignmentWithSchedule(@PathVariable Long id,
            @RequestBody FacultyAssignmentWithScheduleDTO dto, Principal principal) {
        dto.setId(id);
        FacultyAssignment saved = facultyAssignmentService.saveFacultyAssignmentWithSchedule(dto);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Updated faculty assignment (ID: " + id + ") with schedule - Faculty (ID: " + (dto.getFacultyId() != null ? dto.getFacultyId() : "None") + "), Section (ID: " + (dto.getSectionId() != null ? dto.getSectionId() : "None") + "), Course (ID: " + (dto.getCurriculumCourseId() != null ? dto.getCurriculumCourseId() : "None") + ")";
        activityLogService.log(adminUsername, "Updated Faculty Assignment with Schedule", details);
        return ResponseEntity.ok(saved);
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<String> handleScheduleConflict(ScheduleConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
