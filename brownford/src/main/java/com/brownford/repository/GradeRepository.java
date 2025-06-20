package com.brownford.repository;

import com.brownford.model.Grade;
import com.brownford.model.Student;
import com.brownford.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    // Add custom queries if needed

    // Find grade by student, course, semester, and school year
    Optional<Grade> findByStudentAndCourseAndSemesterAndSchoolYear(
            Student student,
            Course course,
            String semester,
            String schoolYear);

    // Find grade by studentId (alphanumeric), courseId, semester, and school year
    @Query("SELECT g FROM Grade g WHERE g.student.studentId = :studentId AND g.course.id = :courseId AND g.semester = :semester AND g.schoolYear = :schoolYear")
    Optional<Grade> findByStudentIdAndCourseIdAndSemesterAndSchoolYear(
            @Param("studentId") String studentId,
            @Param("courseId") Long courseId,
            @Param("semester") String semester,
            @Param("schoolYear") String schoolYear
    );

    // Find all grades for a student
    List<Grade> findByStudent(Student student);

    // Find grade by student, course, and semester (no school year)
    Optional<Grade> findByStudentAndCourseAndSemester(
            Student student,
            Course course,
            String semester);
}
