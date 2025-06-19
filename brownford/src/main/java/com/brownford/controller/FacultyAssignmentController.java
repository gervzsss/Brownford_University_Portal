package com.brownford.controller;

import com.brownford.dto.FacultyAssignmentWithScheduleDTO;
import com.brownford.model.FacultyAssignment;
import com.brownford.model.Schedule;
import com.brownford.service.FacultyAssignmentService;
import com.brownford.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/faculty-assignments")
public class FacultyAssignmentController {
    @Autowired
    private FacultyAssignmentService facultyAssignmentService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public List<FacultyAssignment> getAllFacultyAssignments(
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long curriculumCourseId,
            @RequestParam(required = false) String semester) {
        List<FacultyAssignment> all = facultyAssignmentService.getAllFacultyAssignments();
        if (sectionId != null) {
            all = all.stream().filter(fa -> fa.getSection().getId().equals(sectionId)).toList();
        }
        if (curriculumCourseId != null) {
            all = all.stream().filter(fa -> fa.getCurriculumCourse().getId().equals(curriculumCourseId)).toList();
        }
        if (semester != null) {
            all = all.stream().filter(fa -> fa.getSemester().equalsIgnoreCase(semester)).toList();
        }
        return all;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyAssignment> getFacultyAssignmentById(@PathVariable Long id) {
        Optional<FacultyAssignment> assignment = facultyAssignmentService.getFacultyAssignmentById(id);
        return assignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FacultyAssignment createFacultyAssignment(@RequestBody FacultyAssignment assignment) {
        return facultyAssignmentService.saveFacultyAssignment(assignment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyAssignment> updateFacultyAssignment(@PathVariable Long id, @RequestBody FacultyAssignment updated) {
        Optional<FacultyAssignment> assignmentOpt = facultyAssignmentService.getFacultyAssignmentById(id);
        if (assignmentOpt.isEmpty()) return ResponseEntity.notFound().build();
        FacultyAssignment assignment = assignmentOpt.get();
        assignment.setCurriculumCourse(updated.getCurriculumCourse());
        assignment.setSection(updated.getSection());
        assignment.setFaculty(updated.getFaculty());
        assignment.setSemester(updated.getSemester());
        assignment.setYearLevel(updated.getYearLevel());
        return ResponseEntity.ok(facultyAssignmentService.saveFacultyAssignment(assignment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacultyAssignment(@PathVariable Long id) {
        facultyAssignmentService.deleteFacultyAssignment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/with-schedule")
    public ResponseEntity<FacultyAssignment> createFacultyAssignmentWithSchedule(@RequestBody FacultyAssignmentWithScheduleDTO dto) {
        return ResponseEntity.ok(facultyAssignmentService.saveFacultyAssignmentWithSchedule(dto));
    }

    @PutMapping("/with-schedule/{id}")
    public ResponseEntity<FacultyAssignment> updateFacultyAssignmentWithSchedule(@PathVariable Long id, @RequestBody FacultyAssignmentWithScheduleDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(facultyAssignmentService.saveFacultyAssignmentWithSchedule(dto));
    }
}
