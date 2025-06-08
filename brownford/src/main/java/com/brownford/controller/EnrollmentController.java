package com.brownford.controller;

import com.brownford.model.Enrollment;
import com.brownford.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);
        return enrollment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.saveEnrollment(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable Long id, @RequestBody Enrollment updated) {
        Optional<Enrollment> existing = enrollmentService.getEnrollmentById(id);
        if (existing.isPresent()) {
            Enrollment e = existing.get();
            e.setStudent(updated.getStudent());
            e.setProgram(updated.getProgram());
            e.setSection(updated.getSection());
            e.setYearLevel(updated.getYearLevel());
            e.setEnrolledSubjects(updated.getEnrolledSubjects());
            e.setTotalUnits(updated.getTotalUnits());
            e.setStatus(updated.getStatus());
            return ResponseEntity.ok(enrollmentService.saveEnrollment(e));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
