package com.brownford.controller;

import com.brownford.model.Course;
import com.brownford.model.Enrollment;
import com.brownford.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/student/{studentId}/available-courses")
    public List<Course> getAvailableCourses(@PathVariable Long studentId) {
        return enrollmentService.getAvailableCoursesForStudent(studentId);
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment> getStudentEnrollments(@PathVariable Long studentId) {
        return enrollmentService.getStudentEnrollments(studentId);
    }

    @GetMapping("/student/{studentId}/active")
    public List<Enrollment> getActiveEnrollments(@PathVariable Long studentId) {
        return enrollmentService.getActiveEnrollments(studentId);
    }

    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollInCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam Long sectionId) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudentInCourse(studentId, courseId, sectionId);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{enrollmentId}/drop")
    public ResponseEntity<Enrollment> dropCourse(@PathVariable Long enrollmentId) {
        try {
            Enrollment enrollment = enrollmentService.dropCourse(enrollmentId);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-eligibility")
    public ResponseEntity<Boolean> checkEligibility(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam Long sectionId) {
        try {
            boolean canEnroll = enrollmentService.canEnrollInCourse(studentId, courseId, sectionId);
            return ResponseEntity.ok(canEnroll);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 