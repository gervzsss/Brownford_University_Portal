package com.brownford.service;

import com.brownford.model.User;
import com.brownford.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserIdentifierService {

    @Autowired
    private UserRepository userRepository;

    private static final String STUDENT_PREFIX = "S";
    private static final String FACULTY_PREFIX = "F";
    private static final DateTimeFormatter YEAR_FORMAT = DateTimeFormatter.ofPattern("yy");

    public String generateStudentId() {
        String year = LocalDateTime.now().format(YEAR_FORMAT);
        String prefix = STUDENT_PREFIX + "-" + year + "-";

        // Find the latest student ID for the current year
        String latestId = userRepository.findTopByRoleOrderByStudentIdDesc("student")
                .map(User::getStudentId)
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
        String latestId = userRepository.findTopByRoleOrderByFacultyIdDesc("faculty")
                .map(User::getFacultyId)
                .orElse(prefix + "0000");

        // Extract the numeric part and increment
        String numericPart = latestId.substring(latestId.lastIndexOf("-") + 1);
        int nextNumber = Integer.parseInt(numericPart) + 1;

        // Format with leading zeros
        return prefix + String.format("%04d", nextNumber);
    }

    public void assignIdentifier(User user) {
        if (user.getRole().equalsIgnoreCase("student") && user.getStudentId() == null) {
            user.setStudentId(generateStudentId());
        } else if (user.getRole().equalsIgnoreCase("faculty") && user.getFacultyId() == null) {
            user.setFacultyId(generateFacultyId());
        }
    }
}