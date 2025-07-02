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
import com.brownford.model.CurriculumCourse;
import com.brownford.dto.BatchEnrollmentRequest;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import com.brownford.dto.PendingApprovalDTO;
import java.util.stream.Collectors;
import java.util.Comparator;

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
    @Autowired
    private com.brownford.repository.GradeRepository gradeRepository;
    @Autowired
    private NotificationService notificationService;

    // Helper: get next chronological term
    private static class Term {
        int year;
        int sem; // 1=1st, 2=2nd

        Term(int year, int sem) {
            this.year = year;
            this.sem = sem;
        }

        static Term from(String yearLevel, String semester) {
            int y = Integer.parseInt(yearLevel);
            int s;
            String semLower = semester.toLowerCase();
            if (semLower.contains("1st")) {
                s = 1;
            } else if (semLower.contains("2nd")) {
                s = 2;
            } else {
                throw new IllegalArgumentException(
                        "Invalid semester: " + semester + ". Only '1st' and '2nd' semesters are allowed.");
            }
            return new Term(y, s);
        }

        Term next() {
            if (sem == 1)
                return new Term(year, 2);
            if (sem == 2)
                return new Term(year + 1, 1);
            return new Term(year + 1, 1);
        }

        boolean equals(String yearLevel, String semester) {
            Term t = from(yearLevel, semester);
            return t.year == year && t.sem == sem;
        }
    }

    public Enrollment createEnrollment(Long studentId, List<Long> courseIds, String semester, String yearLevel,
            Long sectionId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        // System.out.println("[DEBUG] createEnrollment called with: studentId=" +
        // studentId + ", semester=" + semester + ", yearLevel=" + yearLevel + ",
        // courseIds=" + courseIds);
        // 1. Check latest enrollment
        Enrollment latest = enrollmentRepository.findTopByStudentOrderByCreatedAtDesc(student);
        if (latest != null) {
            // System.out.println("[DEBUG] Latest enrollment: yearLevel=" +
            // latest.getYearLevel() + ", semester=" + latest.getSemester());
            for (Course prevCourse : latest.getCourses()) {
                String finalGrade = getFinalGradeForCourse(student, prevCourse, latest.getSemester());
                boolean hasFinal = finalGrade != null && !finalGrade.isEmpty();
                // System.out.println("[DEBUG] Previous course: " + prevCourse.getCourseCode() +
                // ", hasFinalGrade=" + hasFinal + (finalGrade != null ? ", finalGrade=" +
                // finalGrade : ", no grade record found"));
                if (!hasFinal) {
                    throw new IllegalStateException(
                            "You must have final grades for all previous subjects before enrolling.");
                }
            }
            // 3. Check chronological order
            Term prev = Term.from(latest.getYearLevel(), latest.getSemester());
            Term next = prev.next();

            if (!next.equals(yearLevel, semester)) {
                throw new IllegalStateException("Enrollment must be chronological. Next allowed: " + next.year
                        + " year, " + (next.sem == 1 ? "1st Semester" : "2nd Semester"));
            }
        }
        List<Course> courses = courseRepository.findAllById(courseIds);
        // Curriculum compliance validation
        if (student.getProgram() != null) {
            Long programId = student.getProgram().getId();
            Optional<com.brownford.model.Curriculum> curriculumOpt = curriculumService
                    .getActiveCurriculumByProgramId(programId);
            if (curriculumOpt.isPresent()) {
                List<CurriculumCourse> allowedCourses = curriculumOpt.get().getCurriculumCourses();
                List<Long> allowedCourseIds = allowedCourses.stream()
                        .filter(cc -> String.valueOf(cc.getYearLevel()).equals(yearLevel)
                                && cc.getSemester().equals(semester))
                        .map(cc -> cc.getCourse().getId())
                        .toList();
                for (Course course : courses) {
                    if (!allowedCourseIds.contains(course.getId())) {
                        throw new IllegalArgumentException("Course " + course.getCourseCode()
                                + " is not allowed for this student's curriculum, year, or semester.");
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
        } else if (latest != null && latest.getSection() != null) {
            // Assign section from latest enrollment if available
            enrollment.setSection(latest.getSection());
        }
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        // Notify all admins of new enrollment request
        String studentName = student.getUser() != null ? student.getUser().getFullName()
                : (student.getStudentId() != null ? student.getStudentId() : "A student");
        String adminMessage = studentName + " has requested enrollment for " + semester + ", Year Level: " + yearLevel
                + ".";
        notificationService.createAdminNotification(adminMessage, "ENROLLMENT_REQUEST");
        return savedEnrollment;
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
        Enrollment saved = enrollmentRepository.save(enrollment);
        // Trigger notification if approved
        if ("APPROVED".equalsIgnoreCase(status)) {
            notificationService.createNotification(
                    enrollment.getStudent(),
                    "Your enrollment for " + enrollment.getSemester() + ", Year Level: " + enrollment.getYearLevel()
                            + " has been approved.",
                    "ENROLLMENT_ACCEPTED",
                    "/student-schedule"
            );
        }
        return saved;
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

    public Enrollment updateEnrollment(Long id, Long studentId, List<Long> courseIds, String semester, String yearLevel,
            Long sectionId) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();
        Student student = studentRepository.findById(studentId).orElseThrow();
        List<Course> courses = courseRepository.findAllById(courseIds);
        enrollment.setStudent(student);
        enrollment.setCourses(courses);
        enrollment.setSemester(semester);
        enrollment.setYearLevel(yearLevel);
        if (sectionId != null) {
            Section section = sectionRepository.findById(sectionId).orElse(null);
            enrollment.setSection(section);
        } else {
            enrollment.setSection(null);
        }
        // Optionally, keep status as is or reset to PENDING
        // enrollment.setStatus("PENDING");
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getFilteredEnrollments(String search, String status, Long programId, String semester) {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return enrollments.stream()
                .filter(e -> (search == null ||
                        (e.getStudent() != null && ((e.getStudent().getStudentId() != null
                                && e.getStudent().getStudentId().toLowerCase().contains(search.toLowerCase())) ||
                                (e.getStudent().getUser() != null && ((e.getStudent().getUser().getFirstName() != null
                                        && e.getStudent().getUser().getFirstName().toLowerCase()
                                                .contains(search.toLowerCase()))
                                        ||
                                        (e.getStudent().getUser().getLastName() != null && e.getStudent().getUser()
                                                .getLastName().toLowerCase().contains(search.toLowerCase()))))))))
                .filter(e -> (status == null || status.isEmpty()
                        || (e.getStatus() != null && e.getStatus().equalsIgnoreCase(status))))
                .filter(e -> (programId == null || (e.getStudent() != null && e.getStudent().getProgram() != null
                        && e.getStudent().getProgram().getId().equals(programId))))
                .filter(e -> (semester == null || semester.isEmpty()
                        || (e.getSemester() != null && e.getSemester().equalsIgnoreCase(semester))))
                .toList();
    }

    /**
     * Returns true if all courses in the latest enrollment have final grades.
     */
    public boolean allPreviousCoursesGraded(Student student, Enrollment latestEnrollment) {
        if (latestEnrollment == null)
            return false;
        for (Course prevCourse : latestEnrollment.getCourses()) {
            boolean hasFinal = gradeRepository
                    .findByStudentAndCourseAndSemester(
                            student, prevCourse, latestEnrollment.getSemester())
                    .map(g -> g.getFinalGrade() != null && !g.getFinalGrade().isEmpty()).orElse(false);
            if (!hasFinal)
                return false;
        }
        return true;
    }

    /**
     * Returns the next chronological term as [yearLevel, semester] given the latest
     * enrollment.
     */
    public String[] getNextTerm(Enrollment latestEnrollment) {
        if (latestEnrollment == null)
            return null;
        int y = Integer.parseInt(latestEnrollment.getYearLevel());
        int s = latestEnrollment.getSemester().toLowerCase().contains("1st") ? 1
                : latestEnrollment.getSemester().toLowerCase().contains("2nd") ? 2 : 3;
        if (s == 1)
            return new String[] { String.valueOf(y), "2nd Semester" };
        if (s == 2)
            return new String[] { String.valueOf(y + 1), "1st Semester" };
        return new String[] { String.valueOf(y + 1), "1st Semester" };
    }

    /**
     * Returns curriculum courses for a given program, yearLevel, and semester.
     */
    public List<CurriculumCourse> getCurriculumCoursesForTerm(Long programId, String yearLevel, String semester) {
        var curriculumOpt = curriculumService.getActiveCurriculumByProgramId(programId);
        if (curriculumOpt.isPresent()) {
            List<CurriculumCourse> allowedCourses = curriculumOpt.get().getCurriculumCourses();
            return allowedCourses.stream()
                    .filter(cc -> String.valueOf(cc.getYearLevel()).equals(yearLevel)
                            && cc.getSemester().equals(semester))
                    .toList();
        }
        return List.of();
    }

    /**
     * Returns the final grade (String) for a student/course/semester, or null if
     * not found.
     */
    public String getFinalGradeForCourse(Student student, Course course, String semester) {
        return gradeRepository
                .findByStudentAndCourseAndSemester(student, course, semester)
                .map(g -> g.getFinalGrade())
                .orElse(null);
    }

    /**
     * Returns all grade records for a student.
     */
    public java.util.List<com.brownford.model.Grade> getAllGradesForStudent(Student student) {
        return gradeRepository.findByStudent(student);
    }

    /**
     * Batch enroll multiple students in the same courses/section/term.
     * Returns a map with lists of successful and failed enrollments.
     */
    public Map<String, Object> batchEnroll(BatchEnrollmentRequest request) {
        Map<String, Object> result = new HashMap<>();
        ArrayList<Long> success = new ArrayList<>();
        ArrayList<Map<String, Object>> failed = new ArrayList<>();
        if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
            result.put("success", success);
            result.put("failed", failed);
            return result;
        }
        for (Long studentId : request.getStudentIds()) {
            try {
                // Use existing createEnrollment logic
                createEnrollment(
                    studentId,
                    request.getCourses(),
                    request.getSemester(),
                    request.getYearLevel(),
                    request.getSectionId()
                );
                success.add(studentId);
            } catch (Exception ex) {
                Map<String, Object> failInfo = new HashMap<>();
                failInfo.put("studentId", studentId);
                failInfo.put("reason", ex.getMessage());
                failed.add(failInfo);
            }
        }
        result.put("success", success);
        result.put("failed", failed);
        return result;
    }

    /**
     * Returns the latest 5 pending enrollments as PendingApprovalDTOs, sorted by request date descending.
     */
    public List<PendingApprovalDTO> getLatestPendingApprovals(int limit) {
        return enrollmentRepository.findByStatus("PENDING").stream()
            .sorted(Comparator.comparing(Enrollment::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
            .limit(limit)
            .map(enrollment -> {
                PendingApprovalDTO dto = new PendingApprovalDTO();
                dto.setEnrollmentId(enrollment.getId());
                if (enrollment.getStudent() != null && enrollment.getStudent().getUser() != null) {
                    dto.setStudentName(enrollment.getStudent().getUser().getFirstName() + " " + enrollment.getStudent().getUser().getLastName());
                } else {
                    dto.setStudentName("N/A");
                }
                if (enrollment.getStudent() != null && enrollment.getStudent().getProgram() != null) {
                    dto.setProgramName(enrollment.getStudent().getProgram().getName());
                } else {
                    dto.setProgramName("N/A");
                }
                dto.setYearLevel(enrollment.getYearLevel());
                dto.setSemester(enrollment.getSemester());
                dto.setRequestDate(enrollment.getCreatedAt());
                return dto;
            })
            .collect(Collectors.toList());
    }

    public Map<String, Object> getLastCoursesAndNextPrerequisites(String studentId, String yearLevel, String semester) {
        Map<String, Object> result = new HashMap<>();
        Student student = studentRepository.findAll().stream()
                .filter(s -> s.getStudentId() != null && s.getStudentId().equals(studentId))
                .findFirst().orElse(null);
        if (student == null) {
            result.put("courses", List.of());
            return result;
        }
        // Find latest enrollment for the given yearLevel and semester
        Enrollment latest = enrollmentRepository.findByStudent(student).stream()
                .filter(e -> e.getYearLevel().equals(yearLevel) && e.getSemester().equals(semester))
                .findFirst().orElse(null);
        if (latest == null) {
            result.put("courses", List.of());
            return result;
        }
        // For each requested course, find its prerequisites from the previous semester
        List<Map<String, Object>> courseDetails = new ArrayList<>();
        // Determine previous term
        int y = Integer.parseInt(yearLevel);
        String prevSemester;
        int prevYear;
        if (semester.toLowerCase().contains("2nd")) {
            prevSemester = "1st Semester";
            prevYear = y;
        } else {
            prevSemester = "2nd Semester";
            prevYear = y - 1;
        }
        for (Course c : latest.getCourses()) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseCode", c.getCourseCode());
            map.put("courseTitle", c.getCourseTitle());
            map.put("units", c.getUnits());
            // Find prerequisites (from previous semester only)
            List<Map<String, Object>> prereqList = new ArrayList<>();
            if (c.getPrerequisites() != null && !c.getPrerequisites().isEmpty()) {
                String[] prereqCodes = c.getPrerequisites().split(",");
                for (String prereqCode : prereqCodes) {
                    String trimmedCode = prereqCode.trim();
                    // Find course by code
                    Course prereq = courseRepository.findAll().stream()
                            .filter(crs -> crs.getCourseCode().equalsIgnoreCase(trimmedCode)).findFirst().orElse(null);
                    if (prereq != null) {
                        // Check if prerequisite is from previous semester
                        boolean isPrevTerm = (prereq.getYearLevel() != null && prereq.getYearLevel() == prevYear)
                                && (prereq.getSemester() != null && prereq.getSemester().equalsIgnoreCase(prevSemester));
                        if (isPrevTerm) {
                            // Fetch remarks from grades table
                            String remarks = gradeRepository
                                .findByStudentAndCourseAndSemester(student, prereq, prevSemester)
                                .map(g -> g.getRemarks()).orElse(null);
                            Map<String, Object> preq = new HashMap<>();
                            preq.put("courseCode", prereq.getCourseCode());
                            preq.put("courseTitle", prereq.getCourseTitle());
                            preq.put("units", prereq.getUnits());
                            preq.put("remarks", remarks);
                            prereqList.add(preq);
                        }
                    }
                }
            }
            map.put("prerequisites", prereqList);
            courseDetails.add(map);
        }
        result.put("courses", courseDetails);
        return result;
    }
}
