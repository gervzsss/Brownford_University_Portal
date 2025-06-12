package com.brownford.controller;

import com.brownford.model.Course;
import com.brownford.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
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
            // Handle multiple program IDs
            java.util.List<Long> programIds = new ArrayList<>();
            if (payload.get("programIds") instanceof java.util.List<?>) {
                java.util.List<?> ids = (java.util.List<?>) payload.get("programIds");
                for (Object pid : ids) {
                    programIds.add(Long.parseLong(pid.toString()));
                }
            }
            Course saved = courseService.createCourse(course, programIds);
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
            java.util.List<Long> programIds = new ArrayList<>();
            if (payload.get("programIds") instanceof java.util.List<?>) {
                java.util.List<?> ids = (java.util.List<?>) payload.get("programIds");
                for (Object pid : ids) {
                    programIds.add(Long.parseLong(pid.toString()));
                }
            }
            Course saved = courseService.updateCourse(id, updated, programIds);
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

    @GetMapping("/by-program-year")
    public List<Course> getCoursesByProgramAndYear(@RequestParam Long programId, @RequestParam Integer yearLevel) {
        return courseService.getCoursesByProgramAndYear(programId, yearLevel);
    }

    @GetMapping("/filter")
    public List<Course> filterCourses(
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) Integer yearLevel,
            @RequestParam(required = false) String semester) {
        // If all filters are present, filter by all
        if (programId != null && yearLevel != null && semester != null) {
            return courseService.findByProgramYearSemester(programId, yearLevel, semester);
        }
        // If only program and yearLevel
        if (programId != null && yearLevel != null) {
            return courseService.getCoursesByProgramAndYear(programId, yearLevel);
        }
        // If only program
        if (programId != null) {
            return courseService.getCoursesByProgram(programId);
        }
        // If no filters, return all
        return courseService.getAllCourses();
    }
}
