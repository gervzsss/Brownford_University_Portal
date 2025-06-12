package com.brownford.controller;

import com.brownford.model.Enrollment;
import com.brownford.model.Course;
import com.brownford.model.User;
import com.brownford.repository.EnrollmentRepository;
import com.brownford.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;
// import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentGradesApiController {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/grades")
    public Map<String, Object> getStudentGrades(@RequestParam String yearLevel, @RequestParam String semester, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) return response;
        String username = principal.getName();
        User student = userRepository.findByUsername(username).orElse(null);
        if (student == null) return response;

        // Find enrollment for the given yearLevel and semester
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
        Enrollment enrollment = enrollments.stream()
            .filter(e -> yearLevel.equals(e.getYearLevel()) && semester.equalsIgnoreCase(e.getSemester()))
            .findFirst().orElse(null);

        List<Map<String, Object>> coursesList = new ArrayList<>();
        if (enrollment != null) {
            for (Course course : enrollment.getCourses()) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("code", course.getCourseCode());
                courseMap.put("title", course.getCourseTitle());
                courseMap.put("units", course.getUnits());
                // Placeholder grades
                courseMap.put("midterm", null);
                courseMap.put("finals", null);
                courseMap.put("finalGrade", null);
                courseMap.put("remarks", null);
                coursesList.add(courseMap);
            }
        }
        response.put("courses", coursesList);
        response.put("semesterGPA", "N/A");
        response.put("cumulativeInfo", Map.of(
            "gpa", "N/A",
            "gradePoint", "N/A",
            "standing", "N/A",
            "totalUnits", "N/A"
        ));
        return response;
    }

    // Renamed to avoid duplicate method signature
    @GetMapping("/grades-map")
    public Map<String, Object> getStudentGradesMap(@RequestParam String yearLevel, @RequestParam String semester, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) return response;
        String username = principal.getName();
        User student = userRepository.findByUsername(username).orElse(null);
        if (student == null) return response;

        // Find enrollment for the given yearLevel and semester
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
        Enrollment enrollment = enrollments.stream()
            .filter(e -> yearLevel.equals(e.getYearLevel()) && semester.equalsIgnoreCase(e.getSemester()))
            .findFirst().orElse(null);

        List<Map<String, Object>> coursesList = new ArrayList<>();
        if (enrollment != null) {
            for (Course course : enrollment.getCourses()) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("code", course.getCourseCode());
                courseMap.put("title", course.getCourseTitle());
                courseMap.put("units", course.getUnits());
                // Placeholder grades
                courseMap.put("midterm", null);
                courseMap.put("finals", null);
                courseMap.put("finalGrade", null);
                courseMap.put("remarks", null);
                coursesList.add(courseMap);
            }
        }
        response.put("courses", coursesList);
        response.put("semesterGPA", "N/A");
        response.put("cumulativeInfo", Map.of(
            "gpa", "N/A",
            "gradePoint", "N/A",
            "standing", "N/A",
            "totalUnits", "N/A"
        ));
        return response;
    }

    @GetMapping("/grade-filters")
    public Map<String, List<String>> getGradeFilters(Principal principal) {
        Map<String, List<String>> filters = new HashMap<>();
        if (principal == null) return filters;
        String username = principal.getName();
        User student = userRepository.findByUsername(username).orElse(null);
        if (student == null) return filters;
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
        Set<String> yearLevels = new LinkedHashSet<>();
        Set<String> semesters = new LinkedHashSet<>();
        for (Enrollment e : enrollments) {
            yearLevels.add(e.getYearLevel());
            semesters.add(e.getSemester());
        }
        filters.put("yearLevels", new ArrayList<>(yearLevels));
        filters.put("semesters", new ArrayList<>(semesters));
        return filters;
    }
}
