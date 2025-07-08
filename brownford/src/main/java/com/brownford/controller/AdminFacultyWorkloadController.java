package com.brownford.controller;

import com.brownford.repository.FacultyRepository;
import com.brownford.repository.FacultyAssignmentRepository;
import com.brownford.repository.ScheduleRepository;
import com.brownford.model.Faculty;
import com.brownford.model.FacultyAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminFacultyWorkloadController {
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/faculty-workload-filters")
    public Map<String, Object> getWorkloadFilters(@RequestParam Long facultyId) {
        Map<String, Object> result = new HashMap<>();
        Optional<Faculty> facultyOpt = facultyRepository.findById(facultyId);
        if (facultyOpt.isEmpty()) return result;
        Faculty faculty = facultyOpt.get();
        List<FacultyAssignment> assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                .toList();
        var schoolYears = assignments.stream().map(FacultyAssignment::getSchoolYear).distinct().sorted().toList();
        var semesters = assignments.stream().map(FacultyAssignment::getSemester).distinct().sorted().toList();
        result.put("schoolYears", schoolYears.stream().map(y -> Map.of("value", y, "label", y)).toList());
        result.put("semesters", semesters.stream().filter(s -> !"Summer".equalsIgnoreCase(s))
                .map(s -> Map.of("value", s, "label", s)).toList());
        return result;
    }

    @GetMapping("/faculty-workload-data")
    public List<Map<String, Object>> getWorkloadData(@RequestParam Long facultyId,
                                                     @RequestParam(required = false) String schoolYear,
                                                     @RequestParam(required = false) String semester) {
        List<Map<String, Object>> result = new ArrayList<>();
        Optional<Faculty> facultyOpt = facultyRepository.findById(facultyId);
        if (facultyOpt.isEmpty()) return result;
        Faculty faculty = facultyOpt.get();
        var assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                .filter(a -> (schoolYear == null || a.getSchoolYear().equals(schoolYear)))
                .filter(a -> (semester == null || a.getSemester().equalsIgnoreCase(semester)))
                .toList();
        int no = 1;
        for (var assignment : assignments) {
            var section = assignment.getSection();
            var curriculumCourse = assignment.getCurriculumCourse();
            var course = curriculumCourse.getCourse();
            var schedules = scheduleRepository.findByCurriculumCourseAndSection(curriculumCourse, section);
            StringBuilder schedStr = new StringBuilder();
            for (var sched : schedules) {
                if (schedStr.length() > 0) schedStr.append(", ");
                schedStr.append(sched.getDay()).append(" ")
                        .append("%02d:%02d-%02d:%02d".formatted(
                        sched.getStartTime().getHour(), sched.getStartTime().getMinute(),
                        sched.getEndTime().getHour(), sched.getEndTime().getMinute()));
            }
            Map<String, Object> row = new HashMap<>();
            row.put("no", no++);
            row.put("section", section.getSectionCode());
            row.put("subjectCode", course.getCourseCode());
            row.put("description", course.getCourseTitle());
            row.put("schedule", schedStr.toString());
            row.put("room", schedules.isEmpty() ? "" : schedules.get(0).getRoom());
            row.put("units", course.getUnits());
            row.put("sectionParam", section.getSectionCode());
            row.put("subjectParam", course.getCourseCode());
            result.add(row);
        }
        return result;
    }
}
