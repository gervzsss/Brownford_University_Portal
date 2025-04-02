package com.brownford.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    private static final Map<String, String> USERS = Map.of(
        "admin", "password",
        "user1", "pass123",
        "asd", "asd");
        
        @PostMapping("/login")
        public String login(@RequestParam String username,
            @RequestParam String password,
            Model model) {
        if (USERS.containsKey(username) && USERS.get(username).equals(password)) {
            return "redirect:/home"; // Redirect if valid user
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Stay on login page
        }
    }
    
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
    
    @GetMapping("/logout")
    public String logoutPage() {
        // Implement logout logic (redirect to login or home page)
        return "redirect:/login";
    }

}
