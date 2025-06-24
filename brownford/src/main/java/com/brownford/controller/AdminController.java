package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.brownford.repository.UserRepository;
import com.brownford.repository.CourseRepository;
import com.brownford.service.NotificationService;
import com.brownford.model.Notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<Notification> allNotifications = notificationService.getNotificationsForAdmin();
        List<Notification> latestNotifications = allNotifications.stream().limit(4).toList();
        model.addAttribute("systemNotifications", latestNotifications);
        return "/admin/admin-dashboard";
    }

    @GetMapping("/admin-programs")
    public String programManagementPage() {
        return "admin/admin-programs";
    }

    @GetMapping("/admin-users")
    public String adminUsers() {
        return "/admin/admin-users";
    }

    @GetMapping("/admin-courses")
    public String adminCourses() {
        return "/admin/admin-courses";
    }

    @GetMapping("/admin-enrollments")
    public String adminEnrollments() {
        return "/admin/admin-enrollments";
    }

    @GetMapping("/admin-sections")
    public String adminSections() {
        return "/admin/admin-sections";
    }

    @GetMapping("/admin-curriculum")
    public String adminCurriculum() {
        return "/admin/admin-curriculum";
    }

    @GetMapping("/admin-assign-schedule")
    public String adminAsignSchedule() {
        return "/admin/admin-assign-schedule";
    }

    @GetMapping("/admin-faculty-list")
    public String adminFacultyList() {
        return "/admin/admin-faculty-list";
    }

    @GetMapping("/admin-faculty-workload")
    public String adminFacultyWorkload() {
        return "/admin/admin-faculty-workload";
    }

    @GetMapping("/admin-student-list")
    public String adminStudentList() {
        return "/admin/admin-student-list";
    }

    @GetMapping("/admin-student-records")
    public String adminStudentRecords() {
        return "/admin/admin-student-records";
    }

    @GetMapping("/api/admin/system-summary")
    @ResponseBody
    public Map<String, Object> getSystemSummary() {
        Map<String, Object> summary = new HashMap<>();
        long totalStudents = userRepository.findAll().stream().filter(u -> "student".equalsIgnoreCase(u.getRole()))
                .count();
        long facultyMembers = userRepository.findAll().stream().filter(u -> "faculty".equalsIgnoreCase(u.getRole()))
                .count();
        long activeCourses = courseRepository.findAll().stream().filter(c -> "Active".equalsIgnoreCase(c.getStatus()))
                .count();
        summary.put("totalStudents", totalStudents);
        summary.put("facultyMembers", facultyMembers);
        summary.put("activeCourses", activeCourses);
        summary.put("classSchedules", 0); // Placeholder for now
        return summary;
    }
}
