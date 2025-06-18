package com.brownford.controller;

import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/curriculums")
public class CurriculumController {
    @Autowired
    private CurriculumService curriculumService;

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
    public Curriculum createCurriculum(@RequestBody Curriculum curriculum) {
        return curriculumService.saveCurriculum(curriculum);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/courses")
    public List<CurriculumCourse> getCoursesForCurriculum(@PathVariable Long id) {
        return curriculumService.getCoursesForCurriculum(id);
    }

    @PostMapping("/{id}/courses")
    public CurriculumCourse addCourseToCurriculum(@PathVariable Long id, @RequestBody CurriculumCourse curriculumCourse) {
        // Set the curriculum reference
        Curriculum curriculum = curriculumService.getCurriculumById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curriculum not found"));
        curriculumCourse.setCurriculum(curriculum);
        return curriculumService.saveCurriculumCourse(curriculumCourse);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCurriculumCourse(@PathVariable Long courseId) {
        curriculumService.deleteCurriculumCourse(courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-program/{programId}/all")
    public List<Curriculum> getCurriculumsByProgram(@PathVariable Long programId) {
        return curriculumService.getCurriculumsByProgramId(programId);
    }

    @GetMapping("/by-program/{programId}/status/{status}")
    public List<Curriculum> getCurriculumsByProgramAndStatus(@PathVariable Long programId, @PathVariable Curriculum.Status status) {
        return curriculumService.getCurriculumsByProgramIdAndStatus(programId, status);
    }

    @GetMapping("/by-program/{programId}/year/{yearEffective}")
    public Curriculum getCurriculumByProgramAndYear(@PathVariable Long programId, @PathVariable int yearEffective) {
        return curriculumService.getCurriculumByProgramIdAndYear(programId, yearEffective);
    }

    @PutMapping("/{id}")
    public Curriculum updateCurriculum(@PathVariable Long id, @RequestBody Curriculum updated) {
        Curriculum curriculum = curriculumService.getCurriculumById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curriculum not found"));
        curriculum.setYearEffective(updated.getYearEffective());
        curriculum.setDescription(updated.getDescription());
        curriculum.setStatus(updated.getStatus());
        return curriculumService.saveCurriculum(curriculum);
    }

    @GetMapping("/by-program/{programId}")
    public Curriculum getActiveCurriculumByProgram(@PathVariable Long programId) {
        return curriculumService.getActiveCurriculumByProgramId(programId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active curriculum found for this program"));
    }
}
