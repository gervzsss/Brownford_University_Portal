package com.brownford.service;

import com.brownford.model.Enrollment;
import com.brownford.model.User;
import com.brownford.model.Course;
import com.brownford.model.Section;
import com.brownford.repository.EnrollmentRepository;
import com.brownford.repository.UserRepository;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SectionRepository sectionRepository;

    public Enrollment createEnrollment(Long studentId, List<Long> courseIds, String semester, String yearLevel, Long sectionId) {
        User student = userRepository.findById(studentId).orElseThrow();
        List<Course> courses = courseRepository.findAllById(courseIds);
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourses(courses);
        enrollment.setSemester(semester);
        enrollment.setYearLevel(yearLevel);
        enrollment.setStatus("PENDING");
        if (sectionId != null) {
            Section section = sectionRepository.findById(sectionId).orElse(null);
            enrollment.setSection(section);
        }
        return enrollmentRepository.save(enrollment);
    }

    // Keep the old method for compatibility (if needed)
    public Enrollment createEnrollment(Long studentId, List<Long> courseIds, String semester, String yearLevel) {
        return createEnrollment(studentId, courseIds, semester, yearLevel, null);
    }

    public List<Enrollment> getEnrollmentsForStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getPendingEnrollments() {
        return enrollmentRepository.findByStatus("PENDING");
    }

    public Optional<Enrollment> getEnrollment(Long id) {
        return enrollmentRepository.findById(id);
    }

    public Enrollment updateEnrollmentStatus(Long id, String status) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    public long countEnrolledStudentsInSection(Long sectionId) {
        return enrollmentRepository.countBySection_Id(sectionId);
    }

    public Enrollment getLatestEnrollmentForStudent(Long studentId) {
        return enrollmentRepository.findTopByStudentIdOrderByCreatedAtDesc(studentId);
    }
}
