package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/student-home")
    public String home() {
        return "/student/student-home";
    }

    @GetMapping("/student-announcements")
    public String announcements() {
        return "redirect:/student-home";
    }

    @GetMapping("/student-events")
    public String events() {
        return "/student/student-events";
    }

    @GetMapping("/student-contact")
    public String contact() {
        return "/student/student-contact";
    }

    @GetMapping("/student-schedule")
    public String schedule() {
        return "/student/student-schedule";
    }

    @GetMapping("/student-grades")
    public String grades() {
        return "/student/student-grades";
    }

    @GetMapping("/student-enrollment")
    public String enrollment() {
        return "/student/student-enrollment";
    }

    @GetMapping("/student-faculty-evaluation")
    public String facultyevaluation() {
        return "/student/student-faculty-evaluation";
    }

    @GetMapping("/student-faculty-evaluation-form")
    public String EvaluationForm() {
        return "/student/student-faculty-evaluation-form";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/login";
    }

    @GetMapping("/student-profile")
    public String profile() {
        return "/student/student-profile";
    }
}
