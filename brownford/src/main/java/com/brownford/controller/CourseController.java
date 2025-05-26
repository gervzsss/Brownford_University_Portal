package com.brownford.controller;

import com.brownford.model.Course;
import com.brownford.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")

public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<Course> getAllCourses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        List<Course> courses = courseRepository.findAll();

        // Filter by search
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            courses = courses.stream().filter(course -> course.getCourseCode().toLowerCase().contains(searchLower) ||
                    course.getCourseTitle().toLowerCase().contains(searchLower)).toList();
        }

        // Sort by specified field and order
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
                courses.sort((c1, c2) -> {
                    switch (sortBy.toLowerCase()) {
                        case "coursecode":
                            return c2.getCourseCode().compareTo(c1.getCourseCode());
                        case "coursetitle":
                            return c2.getCourseTitle().compareTo(c1.getCourseTitle());
                        case "units":
                            return Integer.compare(c2.getUnits(), c1.getUnits());
                        default:
                            return 0;
                    }
                });
            } else {
                courses.sort((c1, c2) -> {
                    switch (sortBy.toLowerCase()) {
                        case "coursecode":
                            return c1.getCourseCode().compareTo(c2.getCourseCode());
                        case "coursetitle":
                            return c1.getCourseTitle().compareTo(c2.getCourseTitle());
                        case "units":
                            return Integer.compare(c1.getUnits(), c2.getUnits());
                        default:
                            return 0;
                    }
                });
            }
        }

        return courses;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        // Only units, not credits
        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Course course = optionalCourse.get();
        course.setCourseCode(courseDetails.getCourseCode());
        course.setCourseTitle(courseDetails.getCourseTitle());
        course.setUnits(courseDetails.getUnits());
        course.setDepartment(courseDetails.getDepartment());
        course.setStatus(courseDetails.getStatus());
        course.setDescription(courseDetails.getDescription());
        course.setPrerequisites(courseDetails.getPrerequisites());
        course.setCorequisites(courseDetails.getCorequisites());
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}