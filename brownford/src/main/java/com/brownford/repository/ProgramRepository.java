package com.brownford.repository;

import com.brownford.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    boolean existsByCode(String code);
}
