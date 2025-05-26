package com.brownford.controller;

import com.brownford.model.Course;
import com.brownford.model.Section;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long id) {
        return sectionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Section createSection(@RequestBody Section section) {
        // Ensure courses are loaded from DB by ID
        if (section.getCourses() != null) {
            java.util.List<Course> attachedCourses = new java.util.ArrayList<>();
            for (Course c : section.getCourses()) {
                courseRepository.findById(c.getId()).ifPresent(attachedCourses::add);
            }
            section.setCourses(attachedCourses);
        }
        return sectionRepository.save(section);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable Long id, @RequestBody Section sectionDetails) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if (optionalSection.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Section section = optionalSection.get();
        // Update courses
        if (sectionDetails.getCourses() != null) {
            java.util.List<Course> attachedCourses = new java.util.ArrayList<>();
            for (Course c : sectionDetails.getCourses()) {
                courseRepository.findById(c.getId()).ifPresent(attachedCourses::add);
            }
            section.setCourses(attachedCourses);
        }
        section.setSectionName(sectionDetails.getSectionName());
        section.setSemester(sectionDetails.getSemester());
        section.setYearLevel(sectionDetails.getYearLevel());
        section.setMaxStudents(sectionDetails.getMaxStudents());
        section.setSchedule(sectionDetails.getSchedule());
        section.setRoom(sectionDetails.getRoom());
        section.setStatus(sectionDetails.getStatus());
        section.setFaculty(sectionDetails.getFaculty());
        return ResponseEntity.ok(sectionRepository.save(section));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        if (!sectionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sectionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
