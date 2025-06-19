package com.brownford.controller;

import com.brownford.model.Enrollment;
import com.brownford.model.Course;
import com.brownford.model.Student;
import com.brownford.model.FacultyAssignment;
import com.brownford.model.Schedule;
import com.brownford.model.Section;
import com.brownford.model.Faculty;
import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.repository.StudentRepository;
import com.brownford.repository.FacultyAssignmentRepository;
import com.brownford.repository.ScheduleRepository;
import com.brownford.repository.CurriculumCourseRepository;
import com.brownford.service.EnrollmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/student")
public class StudentScheduleApiController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CurriculumCourseRepository curriculumCourseRepository;

    @GetMapping("/schedule")
    public List<Map<String, Object>> getStudentSchedule(Principal principal) {
        if (principal == null)
            return Collections.emptyList();
        String username = principal.getName();
        Student student = studentRepository.findAll().stream().filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
        if (student == null)
            return Collections.emptyList();

        // Get latest APPROVED enrollment
        Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
        if (latestEnrollment == null || !"APPROVED".equalsIgnoreCase(latestEnrollment.getStatus())) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Section studentSection = latestEnrollment.getSection(); // Assuming Enrollment has Section
        String semester = latestEnrollment.getSemester(); // Assuming Enrollment has Semester
        int yearLevel = Integer.parseInt(latestEnrollment.getYearLevel()); // Or from enrollment if available
        Curriculum curriculum = studentSection != null ? studentSection.getCurriculum() : null;

        for (Course course : latestEnrollment.getCourses()) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseCode", course.getCourseCode());
            map.put("courseTitle", course.getCourseTitle());

            // Find CurriculumCourse for this course, curriculum, yearLevel, semester
            CurriculumCourse curriculumCourse = null;
            if (curriculum != null) {
                curriculumCourse = curriculumCourseRepository.findAll().stream()
                    .filter(cc -> cc.getCourse().getId().equals(course.getId())
                        && cc.getCurriculum().getId().equals(curriculum.getId())
                        && cc.getYearLevel() == (course.getYearLevel() != null ? course.getYearLevel() : yearLevel)
                        && cc.getSemester().equalsIgnoreCase(course.getSemester() != null ? course.getSemester() : semester))
                    .findFirst().orElse(null);
            }

            // Find faculty assignment for this course, section, semester, year level
            FacultyAssignment assignment = null;
            if (curriculumCourse != null && studentSection != null) {
                assignment = facultyAssignmentRepository
                    .findByCurriculumCourseAndSectionAndSemesterAndYearLevel(
                        curriculumCourse, studentSection, semester, yearLevel)
                    .orElse(null);
            }

            // Find schedule for this course and section
            Schedule schedule = null;
            if (curriculumCourse != null && studentSection != null) {
                schedule = scheduleRepository
                    .findByCurriculumCourseAndSection(curriculumCourse, studentSection)
                    .stream().findFirst().orElse(null);
            }

            // Set schedule details
            map.put("classDays", schedule != null ? schedule.getDay() : "");
            map.put("classTime", schedule != null ?
                (schedule.getStartTime() != null && schedule.getEndTime() != null ?
                    schedule.getStartTime().toString() + " - " + schedule.getEndTime().toString() : "") : "");
            map.put("room", schedule != null ? schedule.getRoom() : "");

            // Set professor name
            Faculty professor = assignment != null ? assignment.getFaculty() : null;
            map.put("professors", (professor != null && professor.getUser() != null) ? professor.getUser().getFullName() : "");

            result.add(map);
        }
        return result;
    }
}
