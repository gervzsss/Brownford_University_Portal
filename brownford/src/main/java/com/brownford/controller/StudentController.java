package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void addStudentToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Student student = studentRepository.findAll().stream()
                    .filter(s -> s.getUser().getUsername().equals(username)).findFirst().orElse(null);
            model.addAttribute("student", student);

            // Add currentSemester and yearLevel for all student pages
            if (student != null) {
                Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
                if (latestEnrollment != null) {
                    model.addAttribute("currentSemester", latestEnrollment.getSemester());
                    model.addAttribute("yearLevel", latestEnrollment.getYearLevel());
                } else {
                    model.addAttribute("currentSemester", "N/A");
                    model.addAttribute("yearLevel", "N/A");
                }
            } else {
                model.addAttribute("currentSemester", "N/A");
                model.addAttribute("yearLevel", "N/A");
            }
        } else {
            model.addAttribute("currentSemester", "N/A");
            model.addAttribute("yearLevel", "N/A");
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
                String enrollmentStatus = "Unknown";
                String currentSemester = null;
                String currentYearLevel = null;
                if (latestEnrollment != null) {
                    currentSemester = latestEnrollment.getSemester();
                    currentYearLevel = latestEnrollment.getYearLevel();
                    enrollmentStatus = latestEnrollment.getStatus();
                    if ("APPROVED".equalsIgnoreCase(enrollmentStatus)) {
                        enrollmentStatus = "ENROLLED";
                    }
                    boolean allGraded = true;
                    for (Course prevCourse : latestEnrollment.getCourses()) {
                        String finalGrade = enrollmentService.getFinalGradeForCourse(student, prevCourse,
                                latestEnrollment.getSemester());
                        boolean hasFinal = finalGrade != null && !finalGrade.isEmpty();
                        if (!hasFinal) {
                            allGraded = false;
                        }
                    }
                    if (allGraded) {
                        // If all previous courses are graded and no enrollment exists for the next
                        // term, set FOR ENROLLMENT
                        String[] nextTerm = enrollmentService.getNextTerm(latestEnrollment);
                        final String nextYearLevelFinal = nextTerm[0];
                        final String nextSemesterFinal = nextTerm[1];
                        nextYearLevel = nextYearLevelFinal;
                        nextSemester = nextSemesterFinal;
                        // Check if student already has enrollment for the next term
                        boolean hasNextEnrollment = enrollmentService.getEnrollmentsForStudent(student.getId()).stream()
                                .anyMatch(e -> nextYearLevelFinal.equals(e.getYearLevel())
                                        && nextSemesterFinal.equals(e.getSemester()));
                        if (!hasNextEnrollment) {
                            enrollmentStatus = "FOR ENROLLMENT";
                        }
                        Long programId = student.getProgram().getId();
                        List<CurriculumCourse> allowedCourses = enrollmentService.getCurriculumCoursesForTerm(programId,
                                nextYearLevel, nextSemester);
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
                        nextSemester = null;
                        nextYearLevel = null;
                        availableCourses = Collections.emptyList();
                    }
                }
                model.addAttribute("availableCourses", availableCourses);
                model.addAttribute("semester", currentSemester); // Info bar uses current
                model.addAttribute("yearLevel", currentYearLevel); // Info bar uses current
                model.addAttribute("enrollmentStatus", enrollmentStatus);
                model.addAttribute("nextSemester", nextSemester); // Next term summary uses next
                model.addAttribute("nextYearLevel", nextYearLevel); // Next term summary uses next
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
        profile.put("middleName", student.getUser().getMiddleName());
        profile.put("lastName", student.getUser().getLastName());
        profile.put("email", student.getUser().getEmail());
        profile.put("program", student.getProgram() != null ? student.getProgram().getName() : "");
        // Use latest enrollment for yearLevel
        String yearLevel = "N/A";
        String semester = "N/A";
        String section = "N/A";
        Enrollment latestEnrollment = enrollmentService.getLatestEnrollmentForStudent(student.getId());
        if (latestEnrollment != null) {
            yearLevel = latestEnrollment.getYearLevel();
            semester = latestEnrollment.getSemester();
            section = latestEnrollment.getSection() != null ? latestEnrollment.getSection().getSectionCode() : "N/A";
        }
        profile.put("yearLevel", yearLevel);
        profile.put("semester", semester);
        profile.put("section", section);
        // Return real values for address, birthday, gender, mobileNumber
        profile.put("address", student.getAddress() != null ? student.getAddress() : "N/A");
        profile.put("dateOfBirth", student.getDateOfBirth() != null ? student.getDateOfBirth() : "N/A");
        profile.put("gender", student.getGender() != null ? student.getGender() : "N/A");
        profile.put("mobileNumber", student.getMobileNumber() != null ? student.getMobileNumber() : "N/A");
        return profile;
    }

    @org.springframework.web.bind.annotation.PutMapping("/api/student/profile-info")
    @ResponseBody
    public Map<String, Object> updateStudentProfileInfo(
            @org.springframework.web.bind.annotation.RequestBody Map<String, String> payload, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("success", false);
            result.put("message", "Not authenticated");
            return result;
        }
        String username = principal.getName();
        Student student = studentRepository.findAll().stream().filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
        if (student == null) {
            result.put("success", false);
            result.put("message", "Student not found");
            return result;
        }
        student.setGender(payload.getOrDefault("gender", student.getGender()));
        student.setDateOfBirth(payload.getOrDefault("birthday", student.getDateOfBirth()));
        student.setMobileNumber(payload.getOrDefault("mobileNumber", student.getMobileNumber()));
        student.setAddress(payload.getOrDefault("address", student.getAddress()));
        studentRepository.save(student);
        result.put("success", true);
        return result;
    }

    @org.springframework.web.bind.annotation.PostMapping("/api/student/change-password")
    @ResponseBody
    public Map<String, Object> changePassword(
            @org.springframework.web.bind.annotation.RequestBody Map<String, String> payload, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("success", false);
            result.put("message", "Not authenticated");
            return result;
        }
        String username = principal.getName();
        Student student = studentRepository.findAll().stream().filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
        if (student == null) {
            result.put("success", false);
            result.put("message", "Student not found");
            return result;
        }
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");
        if (currentPassword == null || newPassword == null) {
            result.put("success", false);
            result.put("message", "Missing password fields");
            return result;
        }
        // Use PasswordEncoder to check current password
        if (!passwordEncoder.matches(currentPassword, student.getUser().getPassword())) {
            result.put("success", false);
            result.put("message", "Current password is incorrect");
            return result;
        }
        student.getUser().setPassword(passwordEncoder.encode(newPassword));
        studentRepository.save(student);
        result.put("success", true);
        return result;
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
