package com.brownford.controller;

import com.brownford.model.Department;
import com.brownford.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        // Check if department with same name or code already exists
        if (departmentRepository.existsByName(department.getName()) || 
            departmentRepository.existsByCode(department.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(departmentRepository.save(department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        try {
            Optional<Department> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Department department = optionalDepartment.get();
            
            // Check if new name or code conflicts with existing departments
            if (!department.getName().equals(departmentDetails.getName()) && 
                departmentRepository.existsByName(departmentDetails.getName())) {
                return ResponseEntity.badRequest()
                    .body(null);
            }
            if (!department.getCode().equals(departmentDetails.getCode()) && 
                departmentRepository.existsByCode(departmentDetails.getCode())) {
                return ResponseEntity.badRequest()
                    .body(null);
            }

            // Validate required fields
            if (departmentDetails.getName() == null || departmentDetails.getName().trim().isEmpty() ||
                departmentDetails.getCode() == null || departmentDetails.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(null);
            }

            department.setName(departmentDetails.getName().trim());
            department.setCode(departmentDetails.getCode().trim());
            department.setDescription(departmentDetails.getDescription() != null ? 
                departmentDetails.getDescription().trim() : null);

            Department savedDepartment = departmentRepository.save(department);
            return ResponseEntity.ok(savedDepartment);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
