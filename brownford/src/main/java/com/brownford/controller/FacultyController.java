package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacultyController {

    @GetMapping("/faculty-dashboard")
    public String facultyDashboard() {
        return "/faculty/faculty-dashboard";
    }

    @GetMapping("/faculty-workload")
    public String facultyWorkload() {
        return "/faculty/faculty-workload";
    }

    @GetMapping("/faculty-class-list")
    public String facultyClassList() {
        return "/faculty/faculty-class-list";
    }

    @GetMapping("/faculty-schedule")
    public String facultySchedule() {
        return "/faculty/faculty-schedule";
    }

    @GetMapping("/faculty-profile")
    public String facultyProfile() {
        return "/faculty/faculty-profile";
    }
}
