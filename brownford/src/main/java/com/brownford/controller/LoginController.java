package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/error")
    public String error() {
        return "/Global/error";
    }
}