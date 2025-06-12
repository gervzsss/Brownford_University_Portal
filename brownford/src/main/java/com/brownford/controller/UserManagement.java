package com.brownford.controller;

import com.brownford.model.User;
import com.brownford.repository.UserRepository;
import com.brownford.service.UserIdentifierService;
import com.brownford.service.EnrollmentService;

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
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/admin/users")
public class UserManagement {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserIdentifierService userIdentifierService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnrollmentService enrollmentService;

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
    public User createUser(@RequestBody Map<String, Object> payload) {
        // Map payload to User object
        User user = new User();
        user.setUsername(payload.get("username").toString());
        user.setFirstName(payload.get("firstName").toString());
        user.setLastName(payload.get("lastName").toString());
        user.setEmail(payload.get("email").toString());
        user.setRole(payload.get("role").toString());
        user.setStatus(payload.get("status").toString());
        if (payload.containsKey("password")) {
            user.setPassword(passwordEncoder.encode(payload.get("password").toString()));
        }
        if (user.getRole().equalsIgnoreCase("student")) {
            if (payload.containsKey("program")) {
                Object programObj = payload.get("program");
                if (programObj instanceof Map<?, ?> programMap && programMap.get("id") != null) {
                    Long programId = Long.valueOf(programMap.get("id").toString());
                    user.setProgram(new com.brownford.model.Program());
                    user.getProgram().setId(programId);
                }
            }
            if (payload.containsKey("yearLevel")) {
                user.setYearLevel(payload.get("yearLevel").toString());
            }
        }
        userIdentifierService.assignIdentifier(user);
        User savedUser = userRepository.save(user);
        // If student, create enrollment
        if (user.getRole().equalsIgnoreCase("student") && payload.containsKey("courses")
                && payload.containsKey("semester")) {
            List<?> courseIdsRaw = (List<?>) payload.get("courses");
            List<Long> courseIds = courseIdsRaw.stream().map(id -> Long.valueOf(id.toString())).toList();
            String semester = payload.get("semester").toString();
            String yearLevel = payload.get("yearLevel") != null ? payload.get("yearLevel").toString() : null;
            enrollmentService.createEnrollment(savedUser.getId(), courseIds, semester, yearLevel);
        }
        return savedUser;
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
            // Hash the password if it's being updated
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setLastLogin(userDetails.getLastLogin());

        if (userDetails.getRole().equalsIgnoreCase("student")) {
            if (userDetails.getProgram() != null) {
                user.setProgram(userDetails.getProgram());
            } else {
                user.setProgram(null);
            }
            // --- FIX: Update yearLevel for students ---
            if (userDetails.getYearLevel() != null && !userDetails.getYearLevel().isEmpty()) {
                user.setYearLevel(userDetails.getYearLevel());
            } else {
                user.setYearLevel(null);
            }
        } else {
            user.setProgram(null);
            user.setYearLevel(null); // Always clear yearLevel for non-students
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Delete all enrollments for this user (student) before deleting the user
        enrollmentService.getEnrollmentsForStudent(id).forEach(enrollment -> {
            enrollmentService.deleteEnrollment(enrollment.getId());
        });
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

    // Get user by studentId (for autofill)
    @GetMapping("/by-student-id/{studentId}")
    public ResponseEntity<User> getUserByStudentId(@PathVariable String studentId) {
        Optional<User> user = userRepository.findByStudentId(studentId);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}