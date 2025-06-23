package com.brownford.service;

import com.brownford.model.Notification;
import com.brownford.model.Student;
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
}
