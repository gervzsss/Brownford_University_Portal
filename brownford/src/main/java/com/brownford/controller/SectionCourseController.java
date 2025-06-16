package com.brownford.controller;

import com.brownford.model.SectionCourse;
import com.brownford.service.SectionCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

@RestController
@RequestMapping("/api/section-courses")
public class SectionCourseController {
    @Autowired
    private SectionCourseService sectionCourseService;

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

    @GetMapping("/{id}")
    public ResponseEntity<SectionCourse> getSectionCourseById(@PathVariable Long id) {
        Optional<SectionCourse> sectionCourse = sectionCourseService.getSectionCourseById(id);
        return sectionCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public SectionCourse createSectionCourse(@RequestBody SectionCourse sectionCourse) {
        return sectionCourseService.saveSectionCourse(sectionCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSectionCourse(@PathVariable Long id) {
        sectionCourseService.deleteSectionCourse(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/section/{sectionId}")
    public List<SectionCourse> getSectionCoursesBySectionId(@PathVariable Long sectionId) {
        return sectionCourseService.getSectionCoursesBySectionId(sectionId);
    }
}
