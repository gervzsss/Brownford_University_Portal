package com.brownford.controller;

import com.brownford.model.Enrollment;
import com.brownford.model.Course;
import com.brownford.model.User;
import com.brownford.repository.EnrollmentRepository;
import com.brownford.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentGradesApiController {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/grades")
    public ResponseEntity<?> getStudentGrades(
            @RequestParam String academicYear,
            @RequestParam String semester,
            Principal principal
    ) {
        // Get the logged-in student
        String username = principal.getName();
        Optional<User> studentOpt = userRepository.findByUsername(username);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        User student = studentOpt.get();
        // Find enrollments for this student, year, and semester (status APPROVED or COMPLETED)
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .filter(e -> e.getSemester().equalsIgnoreCase(semester)
                        && e.getYearLevel().equalsIgnoreCase(academicYear)
                        && (e.getStatus().equalsIgnoreCase("APPROVED") || e.getStatus().equalsIgnoreCase("COMPLETED")))
                .collect(Collectors.toList());
        List<Map<String, Object>> courses = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            for (Course course : enrollment.getCourses()) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("code", course.getCourseCode());
                courseMap.put("title", course.getCourseTitle());
                courseMap.put("units", course.getUnits());
                courseMap.put("midterm", null);
                courseMap.put("finals", null);
                courseMap.put("finalGrade", null);
                courseMap.put("remarks", null);
                courses.add(courseMap);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("courses", courses);
        response.put("semesterGPA", null);
        response.put("cumulativeInfo", null);
        return ResponseEntity.ok(response);
    }
}
