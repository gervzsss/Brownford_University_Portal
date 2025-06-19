package com.brownford.controller;

import com.brownford.model.Schedule;
import com.brownford.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/{id}")
    public Optional<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        // No direct faculty assignment here; use FacultyAssignment endpoints for that
        // logic
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

    public static class ScheduleAssignmentDTO {
        public Long scheduleId;
        public Long facultyId;
        public String day;
        public String startTime;
        public String endTime;
        public String room;
    }
}
