package com.brownford.repository;

import com.brownford.model.Schedule;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCurriculumCourse(CurriculumCourse curriculumCourse);
    List<Schedule> findBySection(Section section);
    List<Schedule> findByCurriculumCourseAndSection(CurriculumCourse curriculumCourse, Section section);
}
