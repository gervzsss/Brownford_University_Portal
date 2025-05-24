package com.brownford.controller;

import com.brownford.model.Grade;
import com.brownford.model.User;
import com.brownford.repository.GradeRepository;
import com.brownford.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeManagementController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all grades for a student
    @GetMapping("/student/{studentId}")
    public List<Grade> getGradesForStudent(@PathVariable Long studentId) {
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null)
            return List.of();
        return gradeRepository.findByStudent(student);
    }

    // Add a grade for a student
    @PostMapping("/student/{studentId}")
    public ResponseEntity<Grade> addGrade(@PathVariable Long studentId, @RequestBody Grade grade) {
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null)
            return ResponseEntity.notFound().build();
        grade.setStudent(student);
        Grade saved = gradeRepository.save(grade);
        return ResponseEntity.ok(saved);
    }

    // Update a grade
    @PutMapping("/{gradeId}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long gradeId, @RequestBody Grade updatedGrade) {
        return gradeRepository.findById(gradeId)
                .map(grade -> {
                    grade.setCourseCode(updatedGrade.getCourseCode());
                    grade.setCourseTitle(updatedGrade.getCourseTitle());
                    grade.setUnits(updatedGrade.getUnits());
                    grade.setGradeValue(updatedGrade.getGradeValue());
                    Grade saved = gradeRepository.save(grade);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a grade
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long gradeId) {
        if (!gradeRepository.existsById(gradeId))
            return ResponseEntity.notFound().build();
        gradeRepository.deleteById(gradeId);
        return ResponseEntity.noContent().build();
    }
}
