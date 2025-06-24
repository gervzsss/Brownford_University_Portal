package com.brownford.service;

import com.brownford.model.Notification;
import com.brownford.model.Student;
import com.brownford.model.Faculty;
import com.brownford.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getNotificationsForStudent(Student student) {
        return notificationRepository.findByStudentOrderByCreatedAtDesc(student);
    }

    public List<Notification> getUnreadNotificationsForStudent(Student student) {
        return notificationRepository.findByStudentAndIsReadFalseOrderByCreatedAtDesc(student);
    }

    public Notification createNotification(Student student, String message, String type) {
        Notification notification = new Notification();
        notification.setStudent(student);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    public void markAsRead(Notification notification) {
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public List<Notification> getNotificationsForFaculty(Faculty faculty) {
        return notificationRepository.findByFacultyOrderByCreatedAtDesc(faculty);
    }

    public List<Notification> getUnreadNotificationsForFaculty(Faculty faculty) {
        return notificationRepository.findByFacultyAndIsReadFalseOrderByCreatedAtDesc(faculty);
    }

    public Notification createNotification(Faculty faculty, String message, String type) {
        Notification notification = new Notification();
        notification.setFaculty(faculty);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForAdmin() {
        return notificationRepository.findByRecipientRoleOrderByCreatedAtDesc("admin");
    }

    public List<Notification> getUnreadNotificationsForAdmin() {
        return notificationRepository.findByRecipientRoleAndIsReadFalseOrderByCreatedAtDesc("admin");
    }

    public Notification createAdminNotification(String message, String type) {
        Notification notification = new Notification();
        notification.setRecipientRole("admin");
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }
}
