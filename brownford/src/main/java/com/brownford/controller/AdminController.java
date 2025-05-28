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

    @GetMapping("/admin-faculty-list")
    public String adminFacultyList() {
        return "/admin/admin-faculty-list";
    }

    @GetMapping("/admin-faculty-workload")
    public String adminFacultyWorkload() {
        return "/admin/admin-faculty-workload";
    }

    @GetMapping("/admin-faculty-evaluation")
    public String adminFacultyEvaluation() {
        return "/admin/admin-faculty-evaluation";
    }

    @GetMapping("/admin-student-list")
    public String adminStudentList() {
        return "/admin/admin-student-list";
    }

    @GetMapping("/admin-student-records")
    public String adminStudentRecords() {
        return "/admin/admin-student-records";
    }

    @GetMapping("/admin-department-management")
    public String adminDepartmentManagement() {
        return "/admin/admin-department-management";
    }
}
