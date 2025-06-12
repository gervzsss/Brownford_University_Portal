package com.brownford.repository;

import com.brownford.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByStatus(String status);

    long countBySection_Id(Long sectionId); // Use Section relation, not sectionId field

    // Add method to get latest enrollment for a student (assuming createdAt is set)
    Enrollment findTopByStudentIdOrderByCreatedAtDesc(Long studentId);
}
