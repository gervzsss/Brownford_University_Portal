package com.brownford.controller;

import com.brownford.model.SectionCourse;
import com.brownford.service.SectionCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/section-courses")
public class SectionCourseController {
    @Autowired
    private SectionCourseService sectionCourseService;

    @GetMapping
    public List<SectionCourse> getAllSectionCourses() {
        return sectionCourseService.getAllSectionCourses();
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
