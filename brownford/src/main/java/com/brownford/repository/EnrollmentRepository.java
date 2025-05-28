package com.brownford.repository;

import com.brownford.model.Enrollment;
import com.brownford.model.User;
import com.brownford.model.Course;
import com.brownford.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    List<Enrollment> findByCourse(Course course);
    List<Enrollment> findBySection(Section section);
    List<Enrollment> findByStudentAndStatus(User student, String status);
    boolean existsByStudentAndCourseAndStatus(User student, Course course, String status);
    int countBySectionAndStatus(Section section, String status);
} 