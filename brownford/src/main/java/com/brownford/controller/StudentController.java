package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import com.brownford.model.UserRepository;
import com.brownford.model.User;
import java.util.Collections;
import java.util.List;
import com.brownford.model.Grade;
import com.brownford.model.GradeRepository;

@Controller
public class StudentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GradeRepository gradeRepository;

    private void addStudentToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User student = userRepository.findByUsername(username).orElse(null);
            model.addAttribute("student", student);
        }
    }

    @GetMapping("/student-home")
    public String home(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-home";
    }

    @GetMapping("/student-announcements")
    public String announcements(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-home";
    }

    @GetMapping("/student-events")
    public String events(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-events";
    }

    @GetMapping("/student-contact")
    public String contact(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-contact";
    }

    @GetMapping("/student-schedule")
    public String schedule(Model model, Principal principal) {
        addStudentToModel(model, principal);
        model.addAttribute("schedules", Collections.emptyList());
        return "/student/student-schedule";
    }

    @GetMapping("/student-grades")
    public String grades(Model model, Principal principal) {
        addStudentToModel(model, principal);
        User student = (User) model.getAttribute("student");
        if (student != null) {
            List<Grade> grades = gradeRepository.findByStudent(student);
            double totalUnits = 0;
            double totalWeighted = 0;
            for (Grade g : grades) {
                totalUnits += g.getUnits();
                totalWeighted += g.getUnits() * g.getGradeValue();
            }
            double gpa = totalUnits > 0 ? totalWeighted / totalUnits : 0;
            model.addAttribute("grades", grades);
            model.addAttribute("gpa", gpa);
            model.addAttribute("totalUnits", totalUnits);
        } else {
            model.addAttribute("grades", java.util.Collections.emptyList());
            model.addAttribute("gpa", 0);
            model.addAttribute("totalUnits", 0);
        }
        return "/student/student-grades";
    }

    @GetMapping("/student-enrollment")
    public String enrollment(Model model, Principal principal) {
        addStudentToModel(model, principal);

        // --- Dummy EnrollmentStatus class for demonstration ---
        // Replace with your real EnrollmentStatus fetching logic
        class EnrollmentStatus {
            private boolean isOpen;
            private String status;
            private String period;
            private String windowDate;

            public EnrollmentStatus(boolean isOpen, String status, String period, String windowDate) {
                this.isOpen = isOpen;
                this.status = status;
                this.period = period;
                this.windowDate = windowDate;
            }
            public boolean isOpen() { return isOpen; }
            public String getStatus() { return status; }
            public String getPeriod() { return period; }
            public String getWindowDate() { return windowDate; }
        }
        EnrollmentStatus enrollmentStatus = new EnrollmentStatus(
            true, // isOpen
            "Open", // status
            "May 20 - June 10, 2025", // period
            "May 20, 2025" // windowDate
        );
        model.addAttribute("enrollmentStatus", enrollmentStatus);

        // --- Dummy Enrollment Steps ---
        class EnrollmentStep {
            public int stepNumber;
            public String title;
            public String description;
            public String status;
            public String actionUrl;
            public String actionText;
            public EnrollmentStep(int stepNumber, String title, String description, String status, String actionUrl, String actionText) {
                this.stepNumber = stepNumber;
                this.title = title;
                this.description = description;
                this.status = status;
                this.actionUrl = actionUrl;
                this.actionText = actionText;
            }
            public int getStepNumber() { return stepNumber; }
            public String getTitle() { return title; }
            public String getDescription() { return description; }
            public String getStatus() { return status; }
            public String getActionUrl() { return actionUrl; }
            public String getActionText() { return actionText; }
        }
        List<EnrollmentStep> enrollmentSteps = List.of(
            new EnrollmentStep(1, "Check Requirements", "Review your prerequisites and requirements.", "Completed", null, null),
            new EnrollmentStep(2, "Select Courses", "Choose your courses for the semester.", "Pending", "/student-courses", "Select Courses"),
            new EnrollmentStep(3, "Confirm Enrollment", "Finalize your enrollment.", "Pending", null, null)
        );
        model.addAttribute("enrollmentSteps", enrollmentSteps);

        // --- Dummy availableCourses and semester for template completeness ---
        model.addAttribute("availableCourses", Collections.emptyList());
        model.addAttribute("semester", "1st Semester");

        return "/student/student-enrollment";
    }

    @GetMapping("/student-faculty-evaluation")
    public String facultyevaluation(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-faculty-evaluation";
    }

    @GetMapping("/student-faculty-evaluation-form")
    public String EvaluationForm(Model model, Principal principal) {
        addStudentToModel(model, principal);
        return "/student/student-faculty-evaluation-form";
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
}
