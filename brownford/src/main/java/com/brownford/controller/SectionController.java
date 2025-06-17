package com.brownford.controller;

import com.brownford.dto.SectionDTO;
import com.brownford.model.Program;
import com.brownford.model.Section;
import com.brownford.service.EnrollmentService;
import com.brownford.service.ProgramService;
import com.brownford.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sections")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ProgramService programService;

    @GetMapping
    public List<SectionDTO> getAllSections() {
        return sectionService.getAllSections().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable Long id) {
        Optional<Section> section = sectionService.getSectionById(id);
        return section.map(s -> ResponseEntity.ok(toDTO(s))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        if (sectionDTO.getProgramId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<Program> programOpt = programService.getProgramById(sectionDTO.getProgramId());
        if (programOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Section section = fromDTO(sectionDTO);
        section.setProgram(programOpt.get());
        Section saved = sectionService.saveSection(section);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> updateSection(@PathVariable Long id, @RequestBody SectionDTO sectionDTO) {
        Optional<Section> existing = sectionService.getSectionById(id);
        if (existing.isPresent()) {
            Section s = existing.get();
            s.setSectionCode(sectionDTO.getSectionCode());
            if (sectionDTO.getProgramId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            Optional<Program> programOpt = programService.getProgramById(sectionDTO.getProgramId());
            if (programOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            s.setProgram(programOpt.get());
            s.setMaxStudents(sectionDTO.getMaxStudents());
            s.setStatus(sectionDTO.getStatus());
            Section updated = sectionService.saveSection(s);
            return ResponseEntity.ok(toDTO(updated));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        try {
            sectionService.deleteSection(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log the error if needed
            return ResponseEntity.status(409).build(); // 409 Conflict
        }
    }

    @GetMapping("/{id}/enrolled-count")
    public ResponseEntity<Long> getEnrolledCount(@PathVariable Long id) {
        long count = enrollmentService.countEnrolledStudentsInSection(id);
        return ResponseEntity.ok(count);
    }

    private SectionDTO toDTO(Section section) {
        SectionDTO dto = new SectionDTO();
        dto.setId(section.getId());
        dto.setSectionCode(section.getSectionCode());
        Program program = section.getProgram();
        if (program != null) {
            dto.setProgramId(program.getId());
            dto.setProgramCode(program.getCode());
            dto.setProgramName(program.getName());
        }
        dto.setMaxStudents(section.getMaxStudents());
        dto.setStatus(section.getStatus());
        return dto;
    }

    private Section fromDTO(SectionDTO dto) {
        Section section = new Section();
        section.setId(dto.getId());
        section.setSectionCode(dto.getSectionCode());
        // program is set in the controller after validation
        section.setMaxStudents(dto.getMaxStudents());
        section.setStatus(dto.getStatus());
        return section;
    }
}
