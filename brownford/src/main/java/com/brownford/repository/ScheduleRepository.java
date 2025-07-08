package com.brownford.repository;

import com.brownford.model.Schedule;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCurriculumCourse(CurriculumCourse curriculumCourse);

    List<Schedule> findBySection(Section section);

    List<Schedule> findByCurriculumCourseAndSection(CurriculumCourse curriculumCourse, Section section);

    // Find schedules for a section that overlap with a given day and time, semester, and school year
    @Query("SELECT s FROM Schedule s JOIN FacultyAssignment fa ON fa.curriculumCourse = s.curriculumCourse AND fa.section = s.section WHERE s.section = :section AND s.day = :day AND fa.semester = :semester AND fa.schoolYear = :schoolYear AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Schedule> findConflictsBySection(@Param("section") Section section, @Param("day") String day, @Param("startTime") java.time.LocalTime startTime, @Param("endTime") java.time.LocalTime endTime, @Param("semester") String semester, @Param("schoolYear") String schoolYear);

    // Find schedules for a faculty that overlap with a given day and time, semester, and school year
    @Query("SELECT s FROM Schedule s JOIN FacultyAssignment fa ON fa.curriculumCourse = s.curriculumCourse AND fa.section = s.section WHERE fa.faculty = :faculty AND s.day = :day AND fa.semester = :semester AND fa.schoolYear = :schoolYear AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Schedule> findConflictsByFaculty(@Param("faculty") com.brownford.model.Faculty faculty, @Param("day") String day, @Param("startTime") java.time.LocalTime startTime, @Param("endTime") java.time.LocalTime endTime, @Param("semester") String semester, @Param("schoolYear") String schoolYear);

    // Find schedules for a room that overlap with a given day and time, semester, and school year
    @Query("SELECT s FROM Schedule s JOIN FacultyAssignment fa ON fa.curriculumCourse = s.curriculumCourse AND fa.section = s.section WHERE s.room = :room AND s.day = :day AND fa.semester = :semester AND fa.schoolYear = :schoolYear AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Schedule> findConflictsByRoom(@Param("room") String room, @Param("day") String day, @Param("startTime") java.time.LocalTime startTime, @Param("endTime") java.time.LocalTime endTime, @Param("semester") String semester, @Param("schoolYear") String schoolYear);
}
