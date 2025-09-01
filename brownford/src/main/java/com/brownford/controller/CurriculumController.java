package com.brownford.controller;

import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.service.ActivityLogService;
import com.brownford.service.CurriculumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/curriculums")
public class CurriculumController {
    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping
    public List<Curriculum> getAllCurriculums() {
        return curriculumService.getAllCurriculums();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curriculum> getCurriculumById(@PathVariable Long id) {
        Optional<Curriculum> curriculum = curriculumService.getCurriculumById(id);
        return curriculum.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Curriculum createCurriculum(@RequestBody Curriculum curriculum, Principal principal) {
        Curriculum saved = curriculumService.saveCurriculum(curriculum);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Created curriculum: " + saved.getDescription() + " (ID: " + saved.getId() + ")";
        activityLogService.log(adminUsername, "Created Curriculum", details);
        return saved;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id, Principal principal) {
        curriculumService.deleteCurriculum(id);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Deleted curriculum with ID: " + id;
        activityLogService.log(adminUsername, "Deleted Curriculum", details);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/courses")
    public List<CurriculumCourse> getCoursesForCurriculum(@PathVariable Long id) {
        return curriculumService.getCoursesForCurriculum(id);
    }

    @PostMapping("/{id}/courses")
    public CurriculumCourse addCourseToCurriculum(@PathVariable Long id,
            @RequestBody CurriculumCourse curriculumCourse, Principal principal) {
        // Set the curriculum reference
        Curriculum curriculum = curriculumService.getCurriculumById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curriculum not found"));
        curriculumCourse.setCurriculum(curriculum);
        CurriculumCourse saved = curriculumService.saveCurriculumCourse(curriculumCourse);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Added course (ID: " + (saved.getCourse() != null ? saved.getCourse().getId() : "None")
                + ") to curriculum (ID: " + id + ")";
        activityLogService.log(adminUsername, "Added Course to Curriculum", details);
        return saved;
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCurriculumCourse(@PathVariable Long courseId, Principal principal) {
        curriculumService.deleteCurriculumCourse(courseId);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Deleted curriculum course with ID: " + courseId;
        activityLogService.log(adminUsername, "Deleted Curriculum Course", details);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-program/{programId}/all")
    public List<Curriculum> getCurriculumsByProgram(@PathVariable Long programId) {
        return curriculumService.getCurriculumsByProgramId(programId);
    }

    @GetMapping("/by-program/{programId}/status/{status}")
    public List<Curriculum> getCurriculumsByProgramAndStatus(@PathVariable Long programId,
            @PathVariable Curriculum.Status status) {
        return curriculumService.getCurriculumsByProgramIdAndStatus(programId, status);
    }

    @GetMapping("/by-program/{programId}/year/{yearEffective}")
    public Curriculum getCurriculumByProgramAndYear(@PathVariable Long programId, @PathVariable int yearEffective) {
        return curriculumService.getCurriculumByProgramIdAndYear(programId, yearEffective);
    }

    @PutMapping("/{id}")
    public Curriculum updateCurriculum(@PathVariable Long id, @RequestBody Curriculum updated, Principal principal) {
        Curriculum curriculum = curriculumService.getCurriculumById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curriculum not found"));
        curriculum.setYearEffective(updated.getYearEffective());
        curriculum.setDescription(updated.getDescription());
        curriculum.setStatus(updated.getStatus());
        Curriculum saved = curriculumService.saveCurriculum(curriculum);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Updated curriculum: " + saved.getDescription() + " (ID: " + saved.getId() + ")";
        activityLogService.log(adminUsername, "Updated Curriculum", details);
        return saved;
    }

    @GetMapping("/by-program/{programId}")
    public Curriculum getActiveCurriculumByProgram(@PathVariable Long programId) {
        return curriculumService.getActiveCurriculumByProgramId(programId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No active curriculum found for this program"));
    }

    @GetMapping("/courses/all")
    public List<CurriculumCourse> getAllCurriculumCourses() {
        return curriculumService.getAllCurriculumCourses();
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CurriculumCourse> getCurriculumCourseById(@PathVariable Long id) {
        Optional<CurriculumCourse> course = curriculumService.getCurriculumCourseById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/courses/{courseId}")
    public CurriculumCourse updateCurriculumCourse(@PathVariable Long courseId, @RequestBody CurriculumCourse updated,
            Principal principal) {
        CurriculumCourse existing = curriculumService.getCurriculumCourseById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curriculum course not found"));
        existing.setYearLevel(updated.getYearLevel());
        existing.setSemester(updated.getSemester());
        existing.setRequired(updated.isRequired());
        existing.setCourse(updated.getCourse());
        // If curriculum can change, add:
        // existing.setCurriculum(updated.getCurriculum());
        CurriculumCourse saved = curriculumService.saveCurriculumCourse(existing);
        // Log admin action
        String adminUsername = principal != null ? principal.getName() : "Unknown";
        String details = "Updated curriculum course (ID: " + courseId + ")";
        activityLogService.log(adminUsername, "Updated Curriculum Course", details);
        return saved;
    }
}
