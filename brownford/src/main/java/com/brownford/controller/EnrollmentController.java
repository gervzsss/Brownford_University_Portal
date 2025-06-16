package com.brownford.controller;

import com.brownford.model.Enrollment;
import com.brownford.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public Enrollment createEnrollment(@RequestBody Map<String, Object> payload) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());
        List<?> courseIdsRaw = (List<?>) payload.get("courses");
        List<Long> courseIds = courseIdsRaw.stream().map(id -> Long.valueOf(id.toString())).toList();
        String semester = payload.get("semester").toString();
        String yearLevel = payload.get("yearLevel").toString();
        Long sectionId = payload.get("sectionId") != null ? Long.valueOf(payload.get("sectionId").toString()) : null;
        return enrollmentService.createEnrollment(studentId, courseIds, semester, yearLevel, sectionId);
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getEnrollmentsForStudent(@PathVariable Long studentId) {
        return enrollmentService.getEnrollmentsForStudent(studentId);
    }

    @GetMapping("/pending")
    public List<Enrollment> getPendingEnrollments() {
        try {
            return enrollmentService.getPendingEnrollments();
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return List.of(); // Return empty list on error
        }
    }

    @PutMapping("/{id}/status")
    public Enrollment updateEnrollmentStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return enrollmentService.updateEnrollmentStatus(id, payload.get("status"));
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }
}
// No major changes needed, but ensure studentId refers to Student entity.
