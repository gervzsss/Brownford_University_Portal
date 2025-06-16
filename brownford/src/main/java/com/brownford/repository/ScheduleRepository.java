package com.brownford.repository;

import com.brownford.model.Schedule;
import com.brownford.model.SectionCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findBySectionCourse(SectionCourse sectionCourse);
}
