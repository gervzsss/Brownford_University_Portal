package com.brownford.controller;

import com.brownford.model.Enrollment;
import com.brownford.model.Course;
import com.brownford.model.Student;
import com.brownford.repository.StudentRepository;
import com.brownford.service.EnrollmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/student")
public class StudentScheduleApiController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/schedule")
    public List<Map<String, Object>> getStudentSchedule(Principal principal) {
        if (principal == null)
            return Collections.emptyList();
        String username = principal.getName();
        Student student = studentRepository.findAll().stream().filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
        if (student == null)
            return Collections.emptyList();

        // Get latest APPROVED enrollment
        Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
        if (latestEnrollment == null || !"APPROVED".equalsIgnoreCase(latestEnrollment.getStatus())) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Course course : latestEnrollment.getCourses()) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseCode", course.getCourseCode());
            map.put("courseTitle", course.getCourseTitle());
            // Placeholders for now
            map.put("classDays", null);
            map.put("classTime", null);
            map.put("room", null);
            map.put("professors", null);
            result.add(map);
        }
        return result;
    }
}
