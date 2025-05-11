package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "/admin/admin-dashboard";
    }

    @GetMapping("/admin-student-assignment")
    public String adminStudentAssignment() {
        return "/admin/admin-student-assignment";
    }

    @GetMapping("/admin-schedule-assignment")
    public String adminScheduleAssignment() {
        return "/admin/admin-schedule-assignment";
    }

    @GetMapping("/admin-subject-assignment")
    public String adminSubjectAssignment() {
        return "/admin/admin-subject-assignment";
    }

    @GetMapping("/admin-users")
    public String adminUsers() {
        return "/admin/admin-users";
    }
}
