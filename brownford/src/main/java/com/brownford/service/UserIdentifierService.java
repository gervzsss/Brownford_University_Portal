package com.brownford.service;

import com.brownford.model.User;
import com.brownford.model.Student;
import com.brownford.model.Faculty;
// import com.brownford.repository.UserRepository;
import com.brownford.repository.StudentRepository;
import com.brownford.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserIdentifierService {

    // @Autowired
    // private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    private static final String STUDENT_PREFIX = "S";
    private static final String FACULTY_PREFIX = "F";
    private static final DateTimeFormatter YEAR_FORMAT = DateTimeFormatter.ofPattern("yy");

    public String generateStudentId() {
        String year = LocalDateTime.now().format(YEAR_FORMAT);
        String prefix = STUDENT_PREFIX + "-" + year + "-";

        // Find the latest student ID for the current year
        String latestId = studentRepository.findAll().stream()
                .map(Student::getStudentId)
                .filter(id -> id != null && id.startsWith(prefix))
                .sorted((a, b) -> b.compareTo(a))
                .findFirst()
                .orElse(prefix + "00000");

        // Extract the numeric part and increment
        String numericPart = latestId.substring(latestId.lastIndexOf("-") + 1);
        int nextNumber = Integer.parseInt(numericPart) + 1;

        // Format with leading zeros
        return prefix + String.format("%05d", nextNumber);
    }

    public String generateFacultyId() {
        String year = LocalDateTime.now().format(YEAR_FORMAT);
        String prefix = FACULTY_PREFIX + "-" + year + "-";

        // Find the latest faculty ID for the current year
        String latestId = facultyRepository.findAll().stream()
                .map(Faculty::getFacultyId)
                .filter(id -> id != null && id.startsWith(prefix))
                .sorted((a, b) -> b.compareTo(a))
                .findFirst()
                .orElse(prefix + "0000");

        // Extract the numeric part and increment
        String numericPart = latestId.substring(latestId.lastIndexOf("-") + 1);
        int nextNumber = Integer.parseInt(numericPart) + 1;

        // Format with leading zeros
        return prefix + String.format("%04d", nextNumber);
    }

    public void assignIdentifier(User user) {
        if (user.getRole().equalsIgnoreCase("student")) {
            Student student = studentRepository.findById(user.getId()).orElse(null);
            if (student != null && (student.getStudentId() == null || student.getStudentId().isEmpty())) {
                student.setStudentId(generateStudentId());
                studentRepository.save(student);
            }
        } else if (user.getRole().equalsIgnoreCase("faculty")) {
            Faculty faculty = facultyRepository.findById(user.getId()).orElse(null);
            if (faculty != null && (faculty.getFacultyId() == null || faculty.getFacultyId().isEmpty())) {
                faculty.setFacultyId(generateFacultyId());
                facultyRepository.save(faculty);
            }
        }
    }
}