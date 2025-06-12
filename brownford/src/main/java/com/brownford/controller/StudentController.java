package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;

import com.brownford.repository.UserRepository;
import com.brownford.model.User;
import com.brownford.service.EnrollmentService;
import com.brownford.model.Enrollment;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StudentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    private void addStudentToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User student = userRepository.findByUsername(username).orElse(null);
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
        if (principal == null) return profile;
        String username = principal.getName();
        User student = userRepository.findByUsername(username).orElse(null);
        if (student == null) return profile;
        profile.put("studentId", student.getStudentId());
        profile.put("firstName", student.getFirstName());
        profile.put("lastName", student.getLastName());
        profile.put("email", student.getEmail());
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
        // Address, birthday, gender, mobileNumber are not in User, so set as N/A
        profile.put("address", "N/A");
        profile.put("dateOfBirth", "N/A");
        profile.put("gender", "N/A");
        profile.put("mobileNumber", "N/A");
        return profile;
    }
}
