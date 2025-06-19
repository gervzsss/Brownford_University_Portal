package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

import com.brownford.repository.FacultyRepository;
import com.brownford.repository.UserRepository;
import com.brownford.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/faculty-grading-sheet")
    public String facultyGradingSheet(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-grading-sheet";
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyRestController {
    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping
    public List<Map<String, Object>> getAllFaculty() {
        return facultyRepository.findAll().stream().map(faculty -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", faculty.getId());
            map.put("facultyId", faculty.getFacultyId());
            map.put("name", faculty.getUser().getFullName());
            return map;
        }).collect(Collectors.toList());
    }
}
