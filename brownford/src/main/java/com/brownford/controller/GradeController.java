package com.brownford.controller;

import com.brownford.model.Grade;
import com.brownford.model.Student;
import com.brownford.model.Course;
import com.brownford.model.Faculty;
import com.brownford.service.GradeService;
import com.brownford.repository.StudentRepository;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.FacultyRepository;
import com.brownford.repository.GradeRepository;
import com.brownford.dto.GradeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private GradeRepository gradeRepository;

    @PostMapping("/encode")
    public ResponseEntity<?> encodeGrade(@RequestBody GradeDTO gradeDTO) {
        Optional<Student> studentOpt = studentRepository.findAll().stream()
                .filter(s -> s.getStudentId().equals(gradeDTO.getStudentId()))
                .findFirst();
        Optional<Course> courseOpt = courseRepository.findById(gradeDTO.getCourseId());
        Optional<Faculty> facultyOpt = facultyRepository.findById(gradeDTO.getFacultyId());
        if (studentOpt.isEmpty() || courseOpt.isEmpty() || facultyOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid student, course, or faculty ID");
        }
        try {
            Grade grade = gradeService.encodeGrade(
                    studentOpt.get(),
                    courseOpt.get(),
                    facultyOpt.get(),
                    gradeDTO.getMidtermGrade(),
                    gradeDTO.getFinalsGrade(),
                    gradeDTO.getSemester(),
                    gradeDTO.getSchoolYear());
            return ResponseEntity.ok(grade);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGrade(@PathVariable Long id) {
        Optional<Grade> gradeOpt = gradeService.findGrade(id);
        return gradeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getGradesForCourseAndTerm(
            @RequestParam Long courseId,
            @RequestParam String semester,
            @RequestParam String schoolYear) {
        try {
            List<Grade> grades = gradeRepository.findAll();
            List<GradeDTO> result = grades.stream()
                    .filter(g -> g.getCourse() != null && g.getCourse().getId().equals(courseId)
                            && g.getSemester() != null && g.getSemester().equals(semester)
                            && g.getSchoolYear() != null && g.getSchoolYear().equals(schoolYear))
                    .map(g -> {
                        GradeDTO dto = new GradeDTO();
                        dto.setStudentId(g.getStudent().getStudentId());
                        dto.setCourseId(g.getCourse().getId());
                        dto.setFacultyId(g.getFaculty().getId());
                        dto.setMidtermGrade(g.getMidtermGrade());
                        dto.setFinalsGrade(g.getFinalsGrade());
                        dto.setFinalGrade(g.getFinalGrade());
                        dto.setRemarks(g.getRemarks());
                        dto.setSemester(g.getSemester());
                        dto.setSchoolYear(g.getSchoolYear());
                        return dto;
                    })
                    .toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching grades: " + e.getMessage());
        }
    }

    @GetMapping("/by-section-course")
    public List<Grade> getGradesBySectionAndCourse(@RequestParam Long sectionId, @RequestParam Long courseId) {
        return gradeRepository.findAll().stream()
                .filter(g -> g.getCourse() != null && g.getCourse().getId().equals(courseId)
                        && g.getStudent() != null && g.getStudent().getProgram() != null
                        && g.getStudent().getProgram().getCurriculums() != null
                        && g.getStudent().getProgram().getCurriculums().stream()
                                .anyMatch(c -> c.getId().equals(sectionId)))
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @RequestBody GradeDTO gradeDTO) {
        return gradeRepository.findById(id)
                .map(grade -> {
                    grade.setMidtermGrade(gradeDTO.getMidtermGrade());
                    grade.setFinalsGrade(gradeDTO.getFinalsGrade());
                    grade.setFinalGrade(gradeDTO.getFinalGrade());
                    gradeRepository.save(grade);
                    return ResponseEntity.ok(grade);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all-for-student")
    public ResponseEntity<?> getAllGradesForStudent(@RequestParam String studentId) {
        try {
            Optional<Student> studentOpt = studentRepository.findAll().stream()
                    .filter(s -> s.getStudentId().equals(studentId))
                    .findFirst();
            if (studentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Student not found");
            }
            List<Grade> grades = gradeRepository.findByStudent(studentOpt.get());
            List<Map<String, Object>> result = grades.stream().map(g -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("studentId", g.getStudent().getStudentId());
                map.put("courseId", g.getCourse().getId());
                map.put("courseCode", g.getCourse().getCourseCode());
                map.put("courseTitle", g.getCourse().getCourseTitle());
                map.put("units", g.getCourse().getUnits());
                map.put("facultyId", g.getFaculty().getId());
                map.put("midtermGrade", g.getMidtermGrade());
                map.put("finalsGrade", g.getFinalsGrade());
                map.put("finalGrade", g.getFinalGrade());
                map.put("remarks", g.getRemarks());
                map.put("semester", g.getSemester());
                map.put("schoolYear", g.getSchoolYear());
                return map;
            }).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching grades: " + e.getMessage());
        }
    }

    // Add more endpoints as needed (delete, search by student/course, etc.)
}
