package com.brownford.repository;

import com.brownford.model.FacultyAssignment;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FacultyAssignmentRepository extends JpaRepository<FacultyAssignment, Long> {
    // Add custom queries if needed
    Optional<FacultyAssignment> findByCurriculumCourseAndSectionAndSemesterAndYearLevel(
        CurriculumCourse curriculumCourse, Section section, String semester, int yearLevel);
}
