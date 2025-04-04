package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/announcements")
    public String announcements() {
        return "redirect:/home";
    }

    @GetMapping("/events")
    public String events() {
        return "events";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/schedule")
    public String schedule() {
        return "schedule";
    }

    @GetMapping("/grades")
    public String grades() {
        return "grades";
    }

    @GetMapping("/enrollment")
    public String enrollment() {
        return "enrollment";
    }

    @GetMapping("/faculty-evaluation")
    public String facultyevaluation() {
        return "faculty-evaluation";
    }

    @GetMapping("/faculty-evaluation-form")
    public String EvaluationForm() {
        return "faculty-evaluation-form";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        // Implement logout logic (redirect to login or home page)
        return "redirect:/login";
    }

}
