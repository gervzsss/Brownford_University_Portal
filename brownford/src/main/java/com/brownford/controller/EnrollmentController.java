package com.brownford.controller;

import com.brownford.dto.BatchEnrollmentRequest;
import com.brownford.dto.CourseInfo;
import com.brownford.dto.EnrollmentDTO;
import com.brownford.dto.PendingApprovalDTO;
import com.brownford.model.Enrollment;
import com.brownford.service.ActivityLogService;
import com.brownford.service.EnrollmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ActivityLogService activityLogService;

    // Utility method to map Enrollment to EnrollmentDTO
    private EnrollmentDTO toDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getStudentId()); // Use real student number
            if (enrollment.getStudent().getUser() != null) {
                dto.setStudentName(enrollment.getStudent().getUser().getFirstName() + " "
                        + enrollment.getStudent().getUser().getLastName());
            }
            if (enrollment.getStudent().getProgram() != null) {
                dto.setProgramName(enrollment.getStudent().getProgram().getName());
            }
        }
        dto.setYearLevel(enrollment.getYearLevel());
        dto.setSemester(enrollment.getSemester());
        dto.setStatus(enrollment.getStatus());
        if (enrollment.getSection() != null) {
            dto.setSectionId(enrollment.getSection().getId());
            dto.setSectionName(enrollment.getSection().getSectionCode());
        }
        // Map courses
        if (enrollment.getCourses() != null) {
            dto.setCourses(enrollment.getCourses().stream()
                    .map(c -> new CourseInfo(c.getId(), c.getCourseCode(), c.getCourseTitle())).toList());
        }
        return dto;
    }

    @PostMapping
    public EnrollmentDTO createEnrollment(@RequestBody Map<String, Object> payload, Principal principal) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        List<?> courseIdsRaw = (List<?>) payload.get("courses");
        List<Long> courseIds = courseIdsRaw.stream().map(id -> Long.valueOf(id.toString())).toList();
        String semester = payload.get("semester").toString();
        String yearLevel = payload.get("yearLevel").toString();
        Long sectionId = payload.get("sectionId") != null ? Long.valueOf(payload.get("sectionId").toString()) : null;
        Enrollment enrollment = enrollmentService.createEnrollment(studentId, courseIds, semester, yearLevel,
                sectionId);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Created enrollment for studentId: " + studentId + ", courses: " + courseIds + ", semester: "
                + semester + ", yearLevel: " + yearLevel;
        activityLogService.log(adminUsername, "Created Enrollment", details);
        return toDTO(enrollment);
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentDTO> getEnrollmentsForStudent(@PathVariable Long studentId) {
        return enrollmentService.getEnrollmentsForStudent(studentId).stream().map(this::toDTO).toList();
    }

    @GetMapping("/pending")
    public List<EnrollmentDTO> getPendingEnrollments() {
        try {
            return enrollmentService.getPendingEnrollments().stream().map(this::toDTO).toList();
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return List.of(); // Return empty list on error
        }
    }

    @PutMapping("/{id}/status")
    public EnrollmentDTO updateEnrollmentStatus(@PathVariable Long id, @RequestBody Map<String, String> payload,
            Principal principal) {
        Enrollment enrollment = enrollmentService.updateEnrollmentStatus(id, payload.get("status"));
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Updated enrollment status for enrollmentId: " + id + " to " + payload.get("status");
        activityLogService.log(adminUsername, "Updated Enrollment Status", details);
        return toDTO(enrollment);
    }

    @GetMapping
    public List<EnrollmentDTO> getAllEnrollments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) String semester) {
        return enrollmentService.getFilteredEnrollments(search, status, programId, semester).stream().map(this::toDTO)
                .toList();
    }

    // New endpoint for fetching a single enrollment by ID
    @GetMapping("/{id}")
    public EnrollmentDTO getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollment(id).map(this::toDTO).orElseThrow();
    }

    @PutMapping("/{id}")
    public EnrollmentDTO updateEnrollment(@PathVariable Long id, @RequestBody Map<String, Object> payload,
            Principal principal) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        List<?> courseIdsRaw = (List<?>) payload.get("courses");
        List<Long> courseIds = courseIdsRaw.stream().map(cid -> Long.valueOf(cid.toString())).toList();
        String semester = payload.get("semester").toString();
        String yearLevel = payload.get("yearLevel").toString();
        Long sectionId = payload.get("sectionId") != null ? Long.valueOf(payload.get("sectionId").toString()) : null;
        Enrollment updated = enrollmentService.updateEnrollment(id, studentId, courseIds, semester, yearLevel,
                sectionId);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Updated enrollment (ID: " + id + ") for studentId: " + studentId + ", courses: " + courseIds
                + ", semester: " + semester + ", yearLevel: " + yearLevel;
        activityLogService.log(adminUsername, "Updated Enrollment", details);
        return toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable Long id, Principal principal) {
        enrollmentService.deleteEnrollment(id);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Deleted enrollment with ID: " + id;
        activityLogService.log(adminUsername, "Deleted Enrollment", details);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batchEnroll(@RequestBody BatchEnrollmentRequest request) {
        // Call service to process batch enrollment
        // Example result structure:
        // { "success": [1,2,3], "failed": [ {"studentId":4, "reason":"Already
        // enrolled"} ] }
        Map<String, Object> result = enrollmentService.batchEnroll(request);
        return ResponseEntity.ok(result);
    }

    // Endpoint: Get latest 5 pending approvals for dashboard summary
    @GetMapping("/pending-approvals/summary")
    public List<PendingApprovalDTO> getLatestPendingApprovalsSummary() {
        return enrollmentService.getLatestPendingApprovals(5);
    }

    // Endpoint to review last enrolled courses and next semester prerequisites for
    // a student
    @GetMapping("/last-courses")
    public ResponseEntity<?> getLastCoursesAndPrerequisites(@RequestParam String studentId,
            @RequestParam String yearLevel, @RequestParam String semester) {
        Map<String, Object> result = enrollmentService.getLastCoursesAndNextPrerequisites(studentId, yearLevel,
                semester);
        return ResponseEntity.ok(result);
    }
}
// No major changes needed, but ensure studentId refers to Student entity.
