package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import com.brownford.model.UserRepository;
import com.brownford.model.User;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "/Global/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/Global/forgot-password";
    }

    @PostMapping("/login")
public String login(@RequestParam String username,
                    @RequestParam String password,
                    Model model) {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
        String role = userOpt.get().getRole();
        if ("admin".equalsIgnoreCase(role)) {
            return "redirect:/admin-dashboard";
        } else if ("faculty".equalsIgnoreCase(role)) {
            return "redirect:/faculty-dashboard";
        } else if ("student".equalsIgnoreCase(role)) {
            return "redirect:/student-home";
        } else {
                return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "redirect:/login?error";
        }
}

    @GetMapping("/error")
    public String error() {
        return "/Global/error";
    }

}