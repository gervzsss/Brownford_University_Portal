package com.brownford.controller;

import com.brownford.model.Notification;
import com.brownford.model.Student;
import com.brownford.repository.StudentRepository;
import com.brownford.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StudentRepository studentRepository;

    private Student getCurrentStudent(Principal principal) {
        if (principal == null) return null;
        String username = principal.getName();
        return studentRepository.findAll().stream()
                .filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
    }

    @GetMapping
    public List<Notification> getNotifications(Principal principal) {
        Student student = getCurrentStudent(principal);
        if (student == null) return List.of();
        return notificationService.getNotificationsForStudent(student);
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id, Principal principal) {
        Student student = getCurrentStudent(principal);
        if (student == null) return;
        notificationService.getNotificationsForStudent(student).stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .ifPresent(notificationService::markAsRead);
    }
}
