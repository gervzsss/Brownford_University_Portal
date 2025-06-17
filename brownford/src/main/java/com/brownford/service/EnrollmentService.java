package com.brownford.service;

import com.brownford.model.Enrollment;
import com.brownford.model.Student;
import com.brownford.model.Course;
import com.brownford.model.Section;
import com.brownford.repository.EnrollmentRepository;
import com.brownford.repository.StudentRepository;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
// import com.brownford.service.CurriculumService;
import com.brownford.model.CurriculumCourse;

@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CurriculumService curriculumService;

    public Enrollment createEnrollment(Long studentId, List<Long> courseIds, String semester, String yearLevel, Long sectionId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        List<Course> courses = courseRepository.findAllById(courseIds);
        // Curriculum compliance validation
        if (student.getProgram() != null) {
            Long programId = student.getProgram().getId();
            Optional<com.brownford.model.Curriculum> curriculumOpt = curriculumService.getCurriculumByProgramId(programId);
            if (curriculumOpt.isPresent()) {
                List<CurriculumCourse> allowedCourses = curriculumOpt.get().getCurriculumCourses();
                List<Long> allowedCourseIds = allowedCourses.stream()
                    .filter(cc -> String.valueOf(cc.getYearLevel()).equals(yearLevel) && cc.getSemester().equals(semester))
                    .map(cc -> cc.getCourse().getId())
                    .toList();
                for (Course course : courses) {
                    if (!allowedCourseIds.contains(course.getId())) {
                        throw new IllegalArgumentException("Course " + course.getCourseCode() + " is not allowed for this student's curriculum, year, or semester.");
                    }
                }
            }
        }
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
        Student student = studentRepository.findById(studentId).orElseThrow();
        return enrollmentRepository.findByStudent(student);
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
        Student student = studentRepository.findById(studentId).orElseThrow();
        return enrollmentRepository.findTopByStudentOrderByCreatedAtDesc(student);
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }
}
