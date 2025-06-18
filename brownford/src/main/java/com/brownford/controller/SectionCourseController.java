package com.brownford.controller;

import com.brownford.model.SectionCourse;
import com.brownford.model.Section;
import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.service.SectionCourseService;
import com.brownford.service.CurriculumService;
import com.brownford.service.SectionService;
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
    @Autowired
    private CurriculumService curriculumService;
    @Autowired
    private SectionService sectionService;

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

    @GetMapping("/curriculum-driven")
    public List<Map<String, Object>> getCurriculumDrivenSectionCourses(@RequestParam Long sectionId, @RequestParam String semester) {
        Section section = sectionService.getSectionById(sectionId).orElseThrow();
        Curriculum curriculum = curriculumService.getActiveCurriculumByProgramId(section.getProgram().getId()).orElseThrow();
        List<CurriculumCourse> curriculumCourses = curriculum.getCurriculumCourses().stream()
            .filter(cc -> cc.getSemester().equalsIgnoreCase(semester))
            .toList();
        List<SectionCourse> sectionCourses = section.getSectionCourses().stream()
            .filter(sc -> sc.getSemester().equalsIgnoreCase(semester))
            .toList();
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (CurriculumCourse cc : curriculumCourses) {
            SectionCourse match = sectionCourses.stream()
                .filter(sc -> sc.getCourse().getId().equals(cc.getCourse().getId()))
                .findFirst().orElse(null);
            Map<String, Object> map = new HashMap<>();
            map.put("curriculumCourseId", cc.getId());
            map.put("courseId", cc.getCourse().getId());
            map.put("courseCode", cc.getCourse().getCourseCode());
            map.put("courseTitle", cc.getCourse().getCourseTitle());
            map.put("yearLevel", cc.getYearLevel());
            map.put("semester", cc.getSemester());
            if (match != null) {
                map.put("sectionCourseId", match.getId());
                map.put("faculty", match.getFaculty() != null ? match.getFaculty().getUser().getFullName() : null);
                map.put("facultyId", match.getFaculty() != null ? match.getFaculty().getId() : null);
                if (match.getSchedules() != null && !match.getSchedules().isEmpty()) {
                    var sched = match.getSchedules().get(0);
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
            } else {
                map.put("sectionCourseId", null);
                map.put("faculty", null);
                map.put("facultyId", null);
                map.put("scheduleId", null);
                map.put("day", null);
                map.put("startTime", null);
                map.put("endTime", null);
                map.put("room", null);
            }
            result.add(map);
        }
        return result;
    }
}
