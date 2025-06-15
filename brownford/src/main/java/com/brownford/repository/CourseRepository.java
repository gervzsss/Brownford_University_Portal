package com.brownford.repository;

import com.brownford.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);
    // Custom queries using CurriculumCourse should be added in CurriculumCourseRepository
}
