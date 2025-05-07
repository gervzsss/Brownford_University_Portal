package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacultyController {

    @GetMapping("/faculty-workload")
    public String facultyWorkload() {
        return "/faculty/faculty-workload";
    }

    @GetMapping("/faculty-grading-sheet")
    public String facultyGradingSheet() {
        return "/faculty/faculty-grading-sheet";
    }

    @GetMapping("/faculty-dashboard")
    public String facultyDashboard() {
        return "/faculty/faculty-dashboard";
    }
}
