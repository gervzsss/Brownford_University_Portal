package com.brownford.service;

import com.brownford.model.Course;
import com.brownford.model.User;
import com.brownford.model.Enrollment;
import com.brownford.model.Section;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.UserRepository;
import com.brownford.repository.EnrollmentRepository;
import com.brownford.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    public List<Course> getAvailableCoursesForStudent(Long studentId) {
        // Get all courses
        List<Course> allCourses = courseRepository.findAll();

        // Filter courses based on department
        return allCourses.stream()
                .collect(Collectors.toList());
    }

    public boolean canEnrollInCourse(Long studentId, Long courseId, Long sectionId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        // Check if student is already enrolled in this course
        if (enrollmentRepository.existsByStudentAndCourseAndStatus(student, course, "ENROLLED")) {
            return false;
        }

        // Check if section is full
        int enrolledCount = enrollmentRepository.countBySectionAndStatus(section, "ENROLLED");
        if (enrolledCount >= section.getMaxStudents()) {
            return false;
        }

        // TODO: Add more enrollment checks
        // - Check if student has completed prerequisites
        // - Check if student's year level matches course requirements
        // - Check if there are no schedule conflicts

        return true;
    }

    @Transactional
    public Enrollment enrollStudentInCourse(Long studentId, Long courseId, Long sectionId) {
        if (!canEnrollInCourse(studentId, courseId, sectionId)) {
            throw new RuntimeException("Student cannot enroll in this course");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSection(section);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus("ENROLLED");

        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public Enrollment dropCourse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus("DROPPED");
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getStudentEnrollments(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudent(student);
    }

    public List<Enrollment> getActiveEnrollments(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudentAndStatus(student, "ENROLLED");
    }
}