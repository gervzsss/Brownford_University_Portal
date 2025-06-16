package com.brownford.controller;

import com.brownford.model.Schedule;
import com.brownford.model.SectionCourse;
import com.brownford.repository.SectionCourseRepository;
import com.brownford.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SectionCourseRepository sectionCourseRepository;

    @GetMapping("/section-course/{sectionCourseId}")
    public List<Schedule> getSchedulesBySectionCourse(@PathVariable Long sectionCourseId) {
        Optional<SectionCourse> sectionCourseOpt = sectionCourseRepository.findById(sectionCourseId);
        return sectionCourseOpt.map(scheduleService::getSchedulesBySectionCourse).orElse(List.of());
    }

    @GetMapping("/{id}")
    public Optional<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.saveSchedule(schedule);
    }

    @PutMapping("/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        schedule.setId(id);
        return scheduleService.saveSchedule(schedule);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }
}
