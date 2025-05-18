package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import org.springframework.ui.Model;


@Controller
public class LoginController {

    

    @GetMapping("/login")
    public String login() {
        return "/Global/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/Global/forgot-password";
    }

    private static final Map<String, String> USERS = Map.of(
            "admin", "qwe",
            "faculty", "zxc",
            "asd", "asd");

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            Model model) {
        if (USERS.containsKey(username) && USERS.get(username).equals(password)) {
            return "redirect:/student-home";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/error")
    public String error() {
        return "/Global/error";
    }

}