package com.brownford.controller;

import com.brownford.model.Course;
import com.brownford.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

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
    public ResponseEntity<Course> createCourse(@RequestBody Map<String, Object> payload) {
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
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
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
            if (saved != null)
                return ResponseEntity.ok(saved);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (courseService.deleteCourse(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
