package com.brownford.repository;

import com.brownford.model.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);

    @Query("SELECT c FROM Course c JOIN c.programs p WHERE p.id = :programId AND c.yearLevel = :yearLevel")
    List<Course> findByProgramIdAndYearLevel(@Param("programId") Long programId, @Param("yearLevel") Integer yearLevel);

    @Query("SELECT c FROM Course c JOIN c.programs p WHERE p.id = :programId")
    List<Course> findByProgramId(@Param("programId") Long programId);

    @Query("SELECT c FROM Course c JOIN c.programs p WHERE p.id = :programId AND c.yearLevel = :yearLevel AND c.semester = :semester")
    List<Course> findByProgramIdAndYearLevelAndSemester(@Param("programId") Long programId, @Param("yearLevel") Integer yearLevel, @Param("semester") String semester);
}
