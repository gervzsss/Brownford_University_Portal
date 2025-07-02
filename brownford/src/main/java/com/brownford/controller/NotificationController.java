package com.brownford.controller;

import com.brownford.model.Notification;
import com.brownford.model.Student;
import com.brownford.model.Faculty;
import com.brownford.repository.StudentRepository;
import com.brownford.repository.FacultyRepository;
import com.brownford.repository.UserRepository;
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
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private UserRepository userRepository;

    private Student getCurrentStudent(Principal principal) {
        if (principal == null)
            return null;
        String username = principal.getName();
        return studentRepository.findAll().stream()
                .filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
    }

    private Faculty getCurrentFaculty(Principal principal) {
        if (principal == null)
            return null;
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .flatMap(user -> facultyRepository.findByUser(user))
                .orElse(null);
    }

    private boolean isAdmin(Principal principal) {
        if (principal == null)
            return false;
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .map(user -> "admin".equals(user.getRole()))
                .orElse(false);
    }

    @GetMapping
    public List<Notification> getNotifications(Principal principal) {
        Student student = getCurrentStudent(principal);
        Faculty faculty = getCurrentFaculty(principal);
        if (student != null) {
            return notificationService.getNotificationsForStudent(student);
        } else if (faculty != null) {
            return notificationService.getNotificationsForFaculty(faculty);
        } else if (isAdmin(principal)) {
            return notificationService.getNotificationsForAdmin();
        }
        return List.of();
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(Principal principal) {
        Student student = getCurrentStudent(principal);
        Faculty faculty = getCurrentFaculty(principal);
        if (student != null) {
            return notificationService.getUnreadNotificationsForStudent(student);
        } else if (faculty != null) {
            return notificationService.getUnreadNotificationsForFaculty(faculty);
        } else if (isAdmin(principal)) {
            return notificationService.getUnreadNotificationsForAdmin();
        }
        return List.of();
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id, Principal principal) {
        Student student = getCurrentStudent(principal);
        Faculty faculty = getCurrentFaculty(principal);
        if (student != null) {
            notificationService.getNotificationsForStudent(student).stream()
                    .filter(n -> n.getId().equals(id))
                    .findFirst()
                    .ifPresent(notificationService::markAsRead);
        } else if (faculty != null) {
            notificationService.getNotificationsForFaculty(faculty).stream()
                    .filter(n -> n.getId().equals(id))
                    .findFirst()
                    .ifPresent(notificationService::markAsRead);
        } else if (isAdmin(principal)) {
            notificationService.getNotificationsForAdmin().stream()
                    .filter(n -> n.getId().equals(id))
                    .findFirst()
                    .ifPresent(notificationService::markAsRead);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id, Principal principal) {
        Student student = getCurrentStudent(principal);
        Faculty faculty = getCurrentFaculty(principal);
        if (student != null) {
            notificationService.getNotificationsForStudent(student).stream()
                    .filter(n -> n.getId().equals(id))
                    .findFirst()
                    .ifPresent(notificationService::deleteNotification);
        } else if (faculty != null) {
            notificationService.getNotificationsForFaculty(faculty).stream()
                    .filter(n -> n.getId().equals(id))
                    .findFirst()
                    .ifPresent(notificationService::deleteNotification);
        } else if (isAdmin(principal)) {
            notificationService.getNotificationsForAdmin().stream()
                    .filter(n -> n.getId().equals(id))
                    .findFirst()
                    .ifPresent(notificationService::deleteNotification);
        }
    }

    @GetMapping("/mark-read-and-redirect/{id}")
    public org.springframework.web.servlet.view.RedirectView markReadAndRedirect(@PathVariable Long id, Principal principal) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null && !notification.isRead()) {
            notificationService.markAsRead(notification);
        }
        String targetUrl = (notification != null && notification.getTargetUrl() != null && !notification.getTargetUrl().isEmpty())
                ? notification.getTargetUrl() : "/";
        return new org.springframework.web.servlet.view.RedirectView(targetUrl);
    }
}
