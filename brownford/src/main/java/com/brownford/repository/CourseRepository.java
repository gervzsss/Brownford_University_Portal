package com.brownford.repository;

import com.brownford.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);

    Optional<Course> findByCourseCode(String courseCode);
}
