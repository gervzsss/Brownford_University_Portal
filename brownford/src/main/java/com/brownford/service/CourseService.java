package com.brownford.service;

import com.brownford.model.Course;
import com.brownford.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updated) {
        return courseRepository.findById(id).map(course -> {
            course.setCourseCode(updated.getCourseCode());
            course.setCourseTitle(updated.getCourseTitle());
            course.setUnits(updated.getUnits());
            course.setPrerequisites(updated.getPrerequisites());
            course.setStatus(updated.getStatus());
            course.setCorequisites(updated.getCorequisites());
            course.setYearLevel(updated.getYearLevel());
            course.setSemester(updated.getSemester());
            return courseRepository.save(course);
        }).orElse(null);
    }

    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
