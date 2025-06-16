package com.brownford.controller;

import com.brownford.model.Schedule;
import com.brownford.model.SectionCourse;
import com.brownford.model.Faculty;
import com.brownford.repository.SectionCourseRepository;
import com.brownford.repository.FacultyRepository;
import com.brownford.service.ScheduleService;
import com.brownford.service.SectionCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Optional;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SectionCourseService sectionCourseService;

    @Autowired
    private SectionCourseRepository sectionCourseRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping
    public List<Map<String, Object>> getAllSectionCourses() {
        return sectionCourseService.getAllSectionCourses().stream().map(sc -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sc.getId());
            map.put("courseCode", sc.getCourse().getCourseCode());
            map.put("courseTitle", sc.getCourse().getCourseTitle());
            map.put("section", sc.getSection().getSectionCode());
            map.put("faculty", sc.getFaculty() != null ? sc.getFaculty().getUser().getFullName() : null);
            map.put("facultyId", sc.getFaculty() != null ? sc.getFaculty().getId() : null);
            map.put("semester", sc.getSemester());
            map.put("yearLevel", sc.getYearLevel());
            if (sc.getSchedules() != null && !sc.getSchedules().isEmpty()) {
                var sched = sc.getSchedules().get(0);
                map.put("scheduleId", sched.getId());
                map.put("day", sched.getDay());
                map.put("startTime", sched.getStartTime() != null ? sched.getStartTime().toString() : null);
                map.put("endTime", sched.getEndTime() != null ? sched.getEndTime().toString() : null);
                map.put("room", sched.getRoom());
            } else {
                map.put("scheduleId", null);
                map.put("day", null);
                map.put("startTime", null);
                map.put("endTime", null);
                map.put("room", null);
            }
            return map;
        }).collect(Collectors.toList());
    }

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

    @PostMapping("/assign")
    public ResponseEntity<?> assignSchedule(@RequestBody ScheduleAssignmentDTO dto) {
        Optional<SectionCourse> scOpt = sectionCourseRepository.findById(dto.sectionCourseId);
        if (scOpt.isEmpty())
            return ResponseEntity.badRequest().body("SectionCourse not found");
        SectionCourse sc = scOpt.get();

        // Assign faculty
        if (dto.facultyId != null) {
            Faculty faculty = facultyRepository.findById(dto.facultyId).orElse(null);
            sc.setFaculty(faculty);
            sectionCourseRepository.save(sc);
        }

        // Create or update schedule (for simplicity, only one schedule per
        // SectionCourse)
        Schedule sched = sc.getSchedules() != null && !sc.getSchedules().isEmpty() ? sc.getSchedules().get(0)
                : new Schedule();
        sched.setSectionCourse(sc);
        sched.setDay(dto.day);
        sched.setStartTime(LocalTime.parse(dto.startTime));
        sched.setEndTime(LocalTime.parse(dto.endTime));
        sched.setRoom(dto.room);
        scheduleService.saveSchedule(sched);

        return ResponseEntity.ok().build();
    }

    public static class ScheduleAssignmentDTO {
        public Long sectionCourseId;
        public Long facultyId;
        public String day;
        public String startTime;
        public String endTime;
        public String room;
    }
}
