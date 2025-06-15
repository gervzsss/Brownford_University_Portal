package com.brownford.controller;

import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Curriculum curriculum = curriculumService.getCurriculumById(id).orElseThrow();
        curriculumCourse.setCurriculum(curriculum);
        return curriculumService.saveCurriculumCourse(curriculumCourse);
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCurriculumCourse(@PathVariable Long courseId) {
        curriculumService.deleteCurriculumCourse(courseId);
        return ResponseEntity.ok().build();
    }
}
