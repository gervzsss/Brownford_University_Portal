package com.brownford.service;

import com.brownford.model.CurriculumCourse;
import com.brownford.model.Schedule;
import com.brownford.model.Section;
import com.brownford.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> findByCurriculumCourseId(Long curriculumCourseId) {
        CurriculumCourse cc = new CurriculumCourse();
        cc.setId(curriculumCourseId);
        return scheduleRepository.findByCurriculumCourse(cc);
    }

    public List<Schedule> findBySectionId(Long sectionId) {
        Section section = new Section();
        section.setId(sectionId);
        return scheduleRepository.findBySection(section);
    }

    public List<Schedule> findByCurriculumCourseIdAndSectionId(Long curriculumCourseId, Long sectionId) {
        CurriculumCourse cc = new CurriculumCourse();
        cc.setId(curriculumCourseId);
        Section section = new Section();
        section.setId(sectionId);
        return scheduleRepository.findByCurriculumCourseAndSection(cc, section);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}
