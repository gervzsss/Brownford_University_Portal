package com.brownford.controller;

import com.brownford.model.User;
import com.brownford.repository.UserRepository;
import com.brownford.service.UserIdentifierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
public class UserManagement {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserIdentifierService userIdentifierService;

    @GetMapping
    public List<User> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        List<User> users = userRepository.findAll();

        // Filter by search
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            users = users.stream().filter(user -> user.getUsername().toLowerCase().contains(searchLower) ||
                    user.getFirstName().toLowerCase().contains(searchLower) ||
                    user.getLastName().toLowerCase().contains(searchLower) ||
                    user.getEmail().toLowerCase().contains(searchLower)).toList();
        }

        // Filter by role
        if (role != null && !role.equalsIgnoreCase("all") && !role.isEmpty()) {
            users = users.stream().filter(user -> user.getRole().equalsIgnoreCase(role)).toList();
        }

        // Filter by status
        if (status != null && !status.equalsIgnoreCase("all") && !status.isEmpty()) {
            users = users.stream().filter(user -> user.getStatus().equalsIgnoreCase(status)).toList();
        }

        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        userIdentifierService.assignIdentifier(user);
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();

        // Only assign new ID if role is changing
        if (!user.getRole().equals(userDetails.getRole())) {
            userIdentifierService.assignIdentifier(userDetails);
        }

        user.setUsername(userDetails.getUsername());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setStatus(userDetails.getStatus());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        user.setLastLogin(userDetails.getLastLogin());

        if (userDetails.getRole().equalsIgnoreCase("student")) {
            if (userDetails.getProgram() != null) {
                user.setProgram(userDetails.getProgram());
            } else {
                user.setProgram(null);
            }
        } else {
            user.setProgram(null);
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public void exportUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        // Reuse your filtering logic
        List<User> users = getAllUsers(search, role, status);

        PrintWriter writer = response.getWriter();
        writer.println("ID,Username,First Name,Last Name,Email,Role,Status");
        for (User user : users) {
            writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    user.getId(),
                    user.getUsername().replace("\"", "\"\""),
                    user.getFirstName().replace("\"", "\"\""),
                    user.getLastName().replace("\"", "\"\""),
                    user.getEmail().replace("\"", "\"\""),
                    user.getRole().replace("\"", "\"\""),
                    user.getStatus().replace("\"", "\"\""));
        }
        writer.flush();
    }

    @GetMapping("/search-students")
    public List<Map<String, String>> searchStudents(@RequestParam String search) {
        List<User> students = userRepository.findAll().stream()
            .filter(u -> "student".equalsIgnoreCase(u.getRole()))
            .filter(u -> {
                String s = search.toLowerCase();
                return (u.getStudentId() != null && u.getStudentId().toLowerCase().contains(s)) ||
                       (u.getFirstName() != null && u.getFirstName().toLowerCase().contains(s)) ||
                       (u.getLastName() != null && u.getLastName().toLowerCase().contains(s)) ||
                       (u.getFullName() != null && u.getFullName().toLowerCase().contains(s));
            })
            .toList();
        List<Map<String, String>> result = new ArrayList<>();
        for (User u : students) {
            if (u.getStudentId() != null) {
                Map<String, String> map = new HashMap<>();
                map.put("studentId", u.getStudentId());
                map.put("fullName", u.getFullName());
                result.add(map);
            }
        }
        return result;
    }
}