package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/home")
    public String home() {
        return "/student/home";
    }

    @GetMapping("/announcements")
    public String announcements() {
        return "redirect:/home";
    }

    @GetMapping("/events")
    public String events() {
        return "/student/events";
    }

    @GetMapping("/contact")
    public String contact() {
        return "/student/contact";
    }

    @GetMapping("/schedule")
    public String schedule() {
        return "/student/schedule";
    }

    @GetMapping("/grades")
    public String grades() {
        return "/student/grades";
    }

    @GetMapping("/enrollment")
    public String enrollment() {
        return "/student/enrollment";
    }

    @GetMapping("/faculty-evaluation")
    public String facultyevaluation() {
        return "/student/faculty-evaluation";
    }

    @GetMapping("/faculty-evaluation-form")
    public String EvaluationForm() {
        return "/student/faculty-evaluation-form";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile() {
        return "/student/profile";
    }
}
