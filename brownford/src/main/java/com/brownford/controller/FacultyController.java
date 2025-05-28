package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;

import com.brownford.repository.UserRepository;
import com.brownford.model.User;

@Controller
public class FacultyController {

    @Autowired
    private UserRepository userRepository;

    private void addFacultyToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User faculty = userRepository.findByUsername(username).orElse(null);
            model.addAttribute("faculty", faculty);
        }
    }

    @GetMapping("/faculty-dashboard")
    public String facultyDashboard(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-dashboard";
    }

    @GetMapping("/faculty-workload")
    public String facultyWorkload(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-workload";
    }

    @GetMapping("/faculty-class-list")
    public String facultyClassList(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-class-list";
    }

    @GetMapping("/faculty-schedule")
    public String facultySchedule(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-schedule";
    }

    @GetMapping("/faculty-profile")
    public String facultyProfile(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-profile";
    }
}
