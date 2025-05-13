package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "/admin/admin-dashboard";
    }

    @GetMapping("/admin-course-section")
    public String adminCourseSection() {
        return "/admin/admin-course-section";
    }

    @GetMapping("/admin-subject-management")
    public String adminSubjectManagement() {
        return "/admin/admin-subject-management";
    }

    @GetMapping("/admin-enrollment-grades")
    public String adminEnrollmentGrades() {
        return "/admin/admin-enrollment-grades";
    }

    @GetMapping("/admin-users")
    public String adminUsers() {
        return "/admin/admin-users";
    }
}
