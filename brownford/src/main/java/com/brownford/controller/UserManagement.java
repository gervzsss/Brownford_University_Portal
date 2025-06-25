package com.brownford.controller;

import com.brownford.model.User;
import com.brownford.model.Student;
import com.brownford.model.Faculty;
import com.brownford.model.Program;
import com.brownford.dto.ProgramDTO;
import com.brownford.repository.UserRepository;
import com.brownford.repository.StudentRepository;
import com.brownford.repository.FacultyRepository;
import com.brownford.repository.ProgramRepository;
import com.brownford.service.UserIdentifierService;
import com.brownford.service.EnrollmentService;
import com.brownford.dto.UserWithRoleIdDTO;

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

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private ProgramRepository programRepository;

    @GetMapping
    public List<UserWithRoleIdDTO> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        List<User> users = userRepository.findAll();

        // Filter by search
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            users = users.stream().filter(user ->
                user.getUsername().toLowerCase().contains(searchLower) ||
                user.getFirstName().toLowerCase().contains(searchLower) ||
                (user.getMiddleName() != null && user.getMiddleName().toLowerCase().contains(searchLower)) ||
                user.getLastName().toLowerCase().contains(searchLower) ||
                user.getEmail().toLowerCase().contains(searchLower)
            ).toList();
        }

        // Filter by role
        if (role != null && !role.equalsIgnoreCase("all") && !role.isEmpty()) {
            users = users.stream().filter(user -> user.getRole().equalsIgnoreCase(role)).toList();
        }

        // Filter by status
        if (status != null && !status.equalsIgnoreCase("all") && !status.isEmpty()) {
            users = users.stream().filter(user -> user.getStatus().equalsIgnoreCase(status)).toList();
        }

        // Map users to DTOs with studentId/facultyId
        List<UserWithRoleIdDTO> dtos = new ArrayList<>();
        for (User user : users) {
            String studentId = null;
            String facultyId = null;
            if (user.getRole().equalsIgnoreCase("student")) {
                Student student = studentRepository.findById(user.getId()).orElse(null);
                if (student != null)
                    studentId = student.getStudentId();
            } else if (user.getRole().equalsIgnoreCase("faculty")) {
                Faculty faculty = facultyRepository.findById(user.getId()).orElse(null);
                if (faculty != null)
                    facultyId = faculty.getFacultyId();
            }
            dtos.add(new UserWithRoleIdDTO(user, studentId, facultyId));
        }
        return dtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("firstName", user.getFirstName());
        result.put("lastName", user.getLastName());
        result.put("email", user.getEmail());
        result.put("role", user.getRole());
        result.put("status", user.getStatus());
        result.put("password", user.getPassword()); // Only for pre-populating the form, not for security
        result.put("lastLogin", user.getLastLogin());
        result.put("middleName", user.getMiddleName());
        if (user.getRole().equalsIgnoreCase("student")) {
            Student student = studentRepository.findById(user.getId()).orElse(null);
            if (student != null) {
                result.put("studentId", student.getStudentId());
                Program program = student.getProgram();
                if (program != null) {
                    ProgramDTO programDTO = new ProgramDTO();
                    programDTO.setId(program.getId());
                    programDTO.setCode(program.getCode());
                    programDTO.setName(program.getName());
                    result.put("program", programDTO);
                } else {
                    result.put("program", null);
                }
            }
        } else if (user.getRole().equalsIgnoreCase("faculty")) {
            Faculty faculty = facultyRepository.findById(user.getId()).orElse(null);
            if (faculty != null) {
                result.put("facultyId", faculty.getFacultyId());
            }
        }
        return ResponseEntity.ok(result);
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
        user.setPassword(passwordEncoder.encode(payload.get("password").toString()));
        user.setMiddleName(payload.get("middleName").toString());
        User savedUser = userRepository.save(user);
        // If student, create Student entity
        if (user.getRole().equalsIgnoreCase("student")) {
            Student student = new Student();
            student.setUser(savedUser);
            // Set program if available
            if (payload.containsKey("programId")) {
                try {
                    Long programId = Long.valueOf(payload.get("programId").toString());
                    programRepository.findById(programId).ifPresent(student::setProgram);
                } catch (Exception e) {
                    // Ignore or log invalid programId
                }
            }
            // Generate and set studentId BEFORE saving
            String studentId = userIdentifierService.generateStudentId();
            student.setStudentId(studentId);
            studentRepository.save(student);
        } else if (user.getRole().equalsIgnoreCase("faculty")) {
            Faculty faculty = new Faculty();
            faculty.setUser(savedUser);
            // Generate and set facultyId BEFORE saving
            String facultyId = userIdentifierService.generateFacultyId();
            faculty.setFacultyId(facultyId);
            facultyRepository.save(faculty);
        }
        return savedUser;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();

        // Only assign new ID if role is changing
        if (!user.getRole().equals(payload.get("role").toString())) {
            userIdentifierService.assignIdentifier(user);
        }

        user.setUsername(payload.get("username").toString());
        user.setFirstName(payload.get("firstName").toString());
        user.setLastName(payload.get("lastName").toString());
        user.setEmail(payload.get("email").toString());
        user.setRole(payload.get("role").toString());
        user.setStatus(payload.get("status").toString());
        user.setMiddleName(payload.get("middleName").toString());
        if (payload.containsKey("password") && payload.get("password") != null
                && !payload.get("password").toString().isEmpty()) {
            user.setPassword(passwordEncoder.encode(payload.get("password").toString()));
        }
        // user.setLastLogin ... (if needed)

        // Update student program if student
        if (user.getRole().equalsIgnoreCase("student")) {
            Student student = studentRepository.findById(user.getId()).orElse(null);
            if (student != null) {
                if (payload.containsKey("programId")) {
                    try {
                        Long programId = Long.valueOf(payload.get("programId").toString());
                        programRepository.findById(programId).ifPresent(student::setProgram);
                    } catch (Exception e) {
                        // Ignore or log invalid programId
                    }
                }
                studentRepository.save(student);
            }
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Delete all enrollments for this user (student) before deleting the user
            if (user.getRole().equalsIgnoreCase("student")) {
                Student student = studentRepository.findById(user.getId()).orElse(null);
                if (student != null) {
                    enrollmentService.getEnrollmentsForStudent(student.getId()).forEach(enrollment -> {
                        enrollmentService.deleteEnrollment(enrollment.getId());
                    });
                    studentRepository.delete(student);
                }
            } else if (user.getRole().equalsIgnoreCase("faculty")) {
                Faculty faculty = facultyRepository.findById(user.getId()).orElse(null);
                if (faculty != null) {
                    facultyRepository.delete(faculty);
                }
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/export")
    public void exportUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        // Fetch and filter users directly for export
        List<User> users = userRepository.findAll();
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            users = users.stream().filter(user -> user.getUsername().toLowerCase().contains(searchLower) ||
                    user.getFirstName().toLowerCase().contains(searchLower) ||
                    user.getLastName().toLowerCase().contains(searchLower) ||
                    user.getEmail().toLowerCase().contains(searchLower)).toList();
        }
        if (role != null && !role.equalsIgnoreCase("all") && !role.isEmpty()) {
            users = users.stream().filter(user -> user.getRole().equalsIgnoreCase(role)).toList();
        }
        if (status != null && !status.equalsIgnoreCase("all") && !status.isEmpty()) {
            users = users.stream().filter(user -> user.getStatus().equalsIgnoreCase(status)).toList();
        }

        PrintWriter writer = response.getWriter();
        writer.println("ID,Username,First Name,Last Name,Email,Role,Status,Student ID,Faculty ID");
        for (User user : users) {
            String studentId = null;
            String facultyId = null;
            if (user.getRole().equalsIgnoreCase("student")) {
                Student student = studentRepository.findById(user.getId()).orElse(null);
                if (student != null)
                    studentId = student.getStudentId();
            } else if (user.getRole().equalsIgnoreCase("faculty")) {
                Faculty faculty = facultyRepository.findById(user.getId()).orElse(null);
                if (faculty != null)
                    facultyId = faculty.getFacultyId();
            }
            writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    user.getId(),
                    user.getUsername().replace("\"", "\"\""),
                    user.getFirstName().replace("\"", "\"\""),
                    user.getLastName().replace("\"", "\"\""),
                    user.getEmail().replace("\"", "\"\""),
                    user.getRole().replace("\"", "\"\""),
                    user.getStatus().replace("\"", "\"\""),
                    studentId != null ? studentId.replace("\"", "\"\"") : "",
                    facultyId != null ? facultyId.replace("\"", "\"\"") : "");
        }
        writer.flush();
    }

    @GetMapping("/search-students")
    public List<Map<String, String>> searchStudents(@RequestParam String search) {
        List<Student> students = studentRepository.findAll();
        String s = search.toLowerCase();
        List<Map<String, String>> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getStudentId() != null &&
                    (student.getStudentId().toLowerCase().contains(s) ||
                            (student.getUser().getFirstName() != null
                                    && student.getUser().getFirstName().toLowerCase().contains(s))
                            ||
                            (student.getUser().getLastName() != null
                                    && student.getUser().getLastName().toLowerCase().contains(s))
                            ||
                            (student.getUser().getFullName() != null
                                    && student.getUser().getFullName().toLowerCase().contains(s)))) {
                Map<String, String> map = new HashMap<>();
                map.put("studentId", student.getStudentId());
                map.put("fullName", student.getUser().getFullName());
                result.add(map);
            }
        }
        return result;
    }

    // Get user by studentId (for autofill)
    @GetMapping("/by-student-id/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable String studentId) {
        Optional<Student> student = studentRepository.findByStudentId(studentId);
        return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}