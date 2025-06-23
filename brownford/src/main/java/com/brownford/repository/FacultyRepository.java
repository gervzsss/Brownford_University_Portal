package com.brownford.repository;

import com.brownford.model.Faculty;
import com.brownford.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByFacultyId(String facultyId);

    Optional<Faculty> findByUser(User user);
}
