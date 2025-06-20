package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.brownford.repository.StudentRepository;
import com.brownford.model.Student;
import com.brownford.service.EnrollmentService;
import com.brownford.model.Enrollment;
import com.brownford.model.Course;
import com.brownford.model.CurriculumCourse;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    private void addStudentToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Student student = studentRepository.findAll().stream()
                    .filter(s -> s.getUser().getUsername().equals(username)).findFirst().orElse(null);
            model.addAttribute("student", student);

            // Add currentSemester for all student pages
            if (student != null) {
                Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
                if (latestEnrollment != null) {
                    model.addAttribute("currentSemester", latestEnrollment.getSemester());
                } else {
                    model.addAttribute("currentSemester", "N/A");
                }
            } else {
                model.addAttribute("currentSemester", "N/A");
            }
        } else {
            model.addAttribute("currentSemester", "N/A");
        }
    }

    @GetMapping("/student-home")
    public String home(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-home";
    }

    @GetMapping("/student-grades")
    public String grades(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-grades";
    }

    @GetMapping("/student-schedule")
    public String schedule(Model model, Principal principal) {
        addStudentToModel(model, principal);
        model.addAttribute("schedules", Collections.emptyList());
        return "/student/student-schedule";
    }

    @GetMapping("/student-enrollment")
    public String enrollment(Model model, Principal principal) {
        addStudentToModel(model, principal);
        if (principal != null) {
            String username = principal.getName();
            Student student = studentRepository.findAll().stream()
                    .filter(s -> s.getUser().getUsername().equals(username)).findFirst().orElse(null);
            if (student != null && student.getProgram() != null) {
                Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
                String nextSemester = null;
                String nextYearLevel = null;
                List<Map<String, Object>> availableCourses = Collections.emptyList();
                if (latestEnrollment != null) {
                    boolean allGraded = true;
                    for (Course prevCourse : latestEnrollment.getCourses()) {
                        String finalGrade = enrollmentService.getFinalGradeForCourse(student, prevCourse, latestEnrollment.getSemester());
                        boolean hasFinal = finalGrade != null && !finalGrade.isEmpty();
                        if (!hasFinal) {
                            allGraded = false;
                        }
                    }
                    if (allGraded) {
                        String[] nextTerm = enrollmentService.getNextTerm(latestEnrollment);
                        nextYearLevel = nextTerm[0];
                        nextSemester = nextTerm[1];
                        Long programId = student.getProgram().getId();
                        List<CurriculumCourse> allowedCourses = enrollmentService.getCurriculumCoursesForTerm(programId, nextYearLevel, nextSemester);
                        availableCourses = allowedCourses.stream()
                            .map(cc -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", cc.getCourse().getId());
                                map.put("code", cc.getCourse().getCourseCode());
                                map.put("title", cc.getCourse().getCourseTitle());
                                map.put("units", cc.getCourse().getUnits());
                                map.put("schedule", "");
                                map.put("instructor", "");
                                map.put("slots", "");
                                return map;
                            })
                            .toList();
                    } else {
                        nextSemester = latestEnrollment.getSemester();
                        nextYearLevel = latestEnrollment.getYearLevel();
                        availableCourses = Collections.emptyList();
                    }
                }
                model.addAttribute("availableCourses", availableCourses);
                model.addAttribute("semester", nextSemester);
                model.addAttribute("yearLevel", nextYearLevel);
            }
        }
        return "/student/student-enrollment";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/login";
    }

    @GetMapping("/student-profile")
    public String profile(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-profile";
    }

    @GetMapping("/api/student/profile-info")
    @ResponseBody
    public Map<String, Object> getStudentProfileInfo(Principal principal) {
        Map<String, Object> profile = new HashMap<>();
        if (principal == null)
            return profile;
        String username = principal.getName();
        Student student = studentRepository.findAll().stream().filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
        if (student == null)
            return profile;
        profile.put("studentId", student.getStudentId());
        profile.put("firstName", student.getUser().getFirstName());
        profile.put("lastName", student.getUser().getLastName());
        profile.put("email", student.getUser().getEmail());
        profile.put("program", student.getProgram() != null ? student.getProgram().getName() : "");
        profile.put("yearLevel", student.getYearLevel());
        // Get latest enrollment for semester and section
        String semester = "N/A";
        String section = "N/A";
        Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
        if (latestEnrollment != null) {
            semester = latestEnrollment.getSemester();
            section = latestEnrollment.getSection() != null ? latestEnrollment.getSection().getSectionCode() : "N/A";
        }
        profile.put("semester", semester);
        profile.put("section", section);
        // Address, birthday, gender, mobileNumber are not in Student, so set as N/A
        profile.put("address", "N/A");
        profile.put("dateOfBirth", "N/A");
        profile.put("gender", "N/A");
        profile.put("mobileNumber", "N/A");
        return profile;
    }
}

@RestController
@RequestMapping("/api/students")
class StudentApiController {
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id).orElse(null);
    }
}
