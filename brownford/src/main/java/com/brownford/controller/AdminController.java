package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "/admin/admin-dashboard";
    }
    
    @GetMapping("/admin-subject-management")
    public String adminSubjectManagement() {
        return "/admin/admin-subject-management";
    }

    @GetMapping("/admin-users")
    public String adminUsers() {
        return "/admin/admin-users";
    }

    @GetMapping("/admin-courses")
    public String adminCourses() {
        return "/admin/admin-courses";
    }

    @GetMapping("/admin-sections")
    public String adminSections() {
        return "/admin/admin-sections";
    }

    @GetMapping("/admin-enrollment")
    public String adminEnrollment() {
        return "/admin/admin-enrollment";
    }

    @GetMapping("/admin-grades")
    public String adminGrades() {
        return "/admin/admin-grades";
    }
}
