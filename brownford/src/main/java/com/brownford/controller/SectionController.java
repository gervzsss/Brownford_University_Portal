package com.brownford.controller;

import com.brownford.model.Section;
import com.brownford.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sections")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping
    public List<Section> getAllSections() {
        return sectionService.getAllSections();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Section> getSectionById(@PathVariable Long id) {
        Optional<Section> section = sectionService.getSectionById(id);
        return section.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Section createSection(@RequestBody Section section) {
        return sectionService.saveSection(section);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Section> updateSection(@PathVariable Long id, @RequestBody Section updated) {
        Optional<Section> existing = sectionService.getSectionById(id);
        if (existing.isPresent()) {
            Section s = existing.get();
            s.setSectionCode(updated.getSectionCode());
            s.setProgram(updated.getProgram());
            s.setYearLevel(updated.getYearLevel());
            s.setSemester(updated.getSemester());
            s.setSchedule(updated.getSchedule());
            s.setRoom(updated.getRoom());
            s.setMaxStudents(updated.getMaxStudents());
            s.setStatus(updated.getStatus());
            return ResponseEntity.ok(sectionService.saveSection(s));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
