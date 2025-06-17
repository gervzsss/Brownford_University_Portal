package com.brownford.controller;

import com.brownford.dto.ProgramDTO;
import com.brownford.model.Program;
import com.brownford.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/programs")
public class ProgramManagement {
    @Autowired
    private ProgramService programService;

    @GetMapping
    public List<ProgramDTO> getAllPrograms() {
        return programService.getAllPrograms().stream().map(this::toDTO).collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDTO> getProgramById(@PathVariable Long id) {
        Optional<Program> program = programService.getProgramById(id);
        return program.map(p -> ResponseEntity.ok(toDTO(p))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProgramDTO> createProgram(@RequestBody ProgramDTO programDTO) {
        Program program = fromDTO(programDTO);
        Program saved = programService.saveProgram(program);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramDTO> updateProgram(@PathVariable Long id, @RequestBody ProgramDTO programDTO) {
        Optional<Program> existing = programService.getProgramById(id);
        if (existing.isPresent()) {
            Program p = existing.get();
            p.setCode(programDTO.getCode());
            p.setName(programDTO.getName());
            p.setYears(programDTO.getYears());
            p.setTotalUnits(programDTO.getTotalUnits());
            p.setStatus(programDTO.getStatus());
            Program updated = programService.saveProgram(p);
            return ResponseEntity.ok(toDTO(updated));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        if (programService.getProgramById(id).isPresent()) {
            programService.deleteProgram(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ProgramDTO toDTO(Program program) {
        ProgramDTO dto = new ProgramDTO();
        dto.setId(program.getId());
        dto.setCode(program.getCode());
        dto.setName(program.getName());
        dto.setYears(program.getYears());
        dto.setTotalUnits(program.getTotalUnits());
        dto.setStatus(program.getStatus());
        return dto;
    }

    private Program fromDTO(ProgramDTO dto) {
        Program program = new Program();
        program.setId(dto.getId());
        program.setCode(dto.getCode());
        program.setName(dto.getName());
        program.setYears(dto.getYears());
        program.setTotalUnits(dto.getTotalUnits());
        program.setStatus(dto.getStatus());
        return program;
    }
}
