package com.brownford.repository;

import com.brownford.model.Notification;
import com.brownford.model.Student;
import com.brownford.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStudentOrderByCreatedAtDesc(Student student);

    List<Notification> findByStudentAndIsReadFalseOrderByCreatedAtDesc(Student student);

    List<Notification> findByFacultyOrderByCreatedAtDesc(Faculty faculty);

    List<Notification> findByFacultyAndIsReadFalseOrderByCreatedAtDesc(Faculty faculty);

    List<Notification> findByRecipientRoleOrderByCreatedAtDesc(String recipientRole);

    List<Notification> findByRecipientRoleAndIsReadFalseOrderByCreatedAtDesc(String recipientRole);
}
