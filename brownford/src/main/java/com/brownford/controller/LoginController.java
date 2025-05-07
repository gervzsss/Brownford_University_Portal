package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Map;

@Controller
public class LoginController {

    // STEP 1: Static users with role and password
    private static final Map<String, User> USERS = Map.of(
        "admin", new User("qwe", "admin"),
        "faculty1", new User("zxc", "faculty"),
        "asd", new User("asd", "student")
    );

    // STEP 2: Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "/Global/login";
    }

    // STEP 3: Handle login and redirect based on role
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        User user = USERS.get(username);

        if (user != null && user.getPassword().equals(password)) {
            String role = user.getRole();
            if (role.equals("admin")) {
                return "redirect:/admin-dashboard";
            } else if (role.equals("faculty")) {
                return "redirect:/faculty-dashboard";
            } else if (role.equals("student")) {
                return "redirect:/home";
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "/Global/login";
    }

    // STEP 4: Dummy pages for redirection
    @GetMapping("/admin")
    public String adminPage() {
        return "/admin/admin-dashboard"; // admin.html
    }

    @GetMapping("/faculty")
    public String facultyPage() {
        return "/faculty/faculty-dashboard"; // faculty.html
    }

    @GetMapping("/student")
    public String studentPage() {
        return "/student/home"; // student.html
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/Global/forgot-password";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
