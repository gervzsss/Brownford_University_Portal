package com.brownford.controller;

import com.brownford.model.Course;
import com.brownford.service.ActivityLogService;
import com.brownford.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping
    public Iterable<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Course course = new Course();
            course.setCourseCode((String) payload.get("courseCode"));
            course.setCourseTitle((String) payload.get("courseTitle"));
            course.setUnits(Integer.parseInt(payload.get("units").toString()));
            course.setPrerequisites((String) payload.get("prerequisites"));
            course.setCorequisites((String) payload.get("corequisites"));
            course.setStatus((String) payload.get("status"));
            course.setSemester((String) payload.get("courseSemester"));
            Integer yearLevel = null;
            if (payload.get("yearLevel") != null && !payload.get("yearLevel").toString().isEmpty()) {
                yearLevel = Integer.parseInt(payload.get("yearLevel").toString());
            }
            course.setYearLevel(yearLevel);
            Course saved = courseService.createCourse(course);
            // Log admin action
            String adminUsername = principal != null ? principal.getName() : "Unknown";
            String details = "Created course: " + course.getCourseCode() + " - " + course.getCourseTitle();
            activityLogService.log(adminUsername, "Created Course", details);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Course updated = new Course();
            updated.setCourseCode((String) payload.get("courseCode"));
            updated.setCourseTitle((String) payload.get("courseTitle"));
            updated.setUnits(Integer.parseInt(payload.get("units").toString()));
            updated.setPrerequisites((String) payload.get("prerequisites"));
            updated.setCorequisites((String) payload.get("corequisites"));
            updated.setStatus((String) payload.get("status"));
            updated.setSemester((String) payload.get("courseSemester"));
            Integer yearLevel = null;
            if (payload.get("yearLevel") != null && !payload.get("yearLevel").toString().isEmpty()) {
                yearLevel = Integer.parseInt(payload.get("yearLevel").toString());
            }
            updated.setYearLevel(yearLevel);
            Course saved = courseService.updateCourse(id, updated);
            if (saved != null) {
                // Log admin action
                String adminUsername = principal != null ? principal.getName() : "Unknown";
                String details = "Updated course: " + updated.getCourseCode() + " - " + updated.getCourseTitle() + " (ID: " + id + ")";
                activityLogService.log(adminUsername, "Updated Course", details);
                return ResponseEntity.ok(saved);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id, Principal principal) {
        if (courseService.deleteCourse(id)) {
            // Log admin action
            String adminUsername = principal != null ? principal.getName() : "Unknown";
            String details = "Deleted course with ID: " + id;
            activityLogService.log(adminUsername, "Deleted Course", details);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
