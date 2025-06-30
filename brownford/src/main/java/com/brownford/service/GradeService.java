package com.brownford.service;

import com.brownford.model.Grade;
import com.brownford.model.Student;
import com.brownford.model.Course;
import com.brownford.model.Faculty;
import com.brownford.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {
    private static final List<String> ALLOWED_GRADES = Arrays.asList(
            "1.00", "1.25", "1.50", "1.75", "2.00", "2.25", "2.50", "2.75", "3.00", "5.00", "INC", "DRP");

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private NotificationService notificationService;

    public boolean isValidGradeValue(String gradeValue) {
        return ALLOWED_GRADES.contains(gradeValue);
    }

    public String getRemarksForGrade(String gradeValue) {
        if (gradeValue == null) {
            return "Pending";
        }
        switch (gradeValue) {
            case "INC":
                return "Incomplete";
            case "DRP":
                return "Dropped";
        }
        try {
            double grade = Double.parseDouble(gradeValue);
            if (grade >= 1.00 && grade <= 3.00) {
                return "PASSED";
            } else if (grade > 3.00) {
                return "FAILED";
            }
        } catch (NumberFormatException e) {
            // Not a numeric grade, already handled above
        }
        return "";
    }

    @Transactional
    public Grade encodeGrade(Student student, Course course, Faculty faculty, String midtermGrade, String finalsGrade,
            String semester, String schoolYear) {
        // Try to find existing grade for upsert using studentId and courseId
        Optional<Grade> existingOpt = gradeRepository.findByStudentIdAndCourseIdAndSemesterAndSchoolYear(
                student.getStudentId(), course.getId(), semester, schoolYear);
        Grade grade = existingOpt.orElseGet(Grade::new);
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setFaculty(faculty);
        grade.setMidtermGrade(midtermGrade);
        grade.setFinalsGrade(finalsGrade);
        // Compute finalGrade if both are present, else set to null
        if (midtermGrade != null && finalsGrade != null && isValidGradeValue(midtermGrade)
                && isValidGradeValue(finalsGrade)) {
            try {
                double mid = Double.parseDouble(midtermGrade);
                double fin = Double.parseDouble(finalsGrade);
                grade.setFinalGrade(String.format("%.2f", (mid + fin) / 2));
            } catch (NumberFormatException e) {
                grade.setFinalGrade(null);
            }
        } else {
            grade.setFinalGrade(null);
        }
        // Set remarks based on finalGrade (handles null)
        grade.setRemarks(getRemarksForGrade(grade.getFinalGrade()));
        grade.setSemester(semester);
        grade.setSchoolYear(schoolYear);
        grade.setDateEncoded(java.time.LocalDateTime.now());
        Grade saved = gradeRepository.save(grade);
        // Trigger notification for grade release
        notificationService.createNotification(
            student,
            "A new grade has been released for course: " + course.getCourseTitle() + ".",
            "GRADE_RELEASED"
        );
        return saved;
    }

    public Optional<Grade> findGrade(Long id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> findAllGrades() {
        return gradeRepository.findAll();
    }

    // Add more methods as needed (update, delete, search by student/course, etc.)
}
