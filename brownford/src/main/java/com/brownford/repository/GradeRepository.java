package com.brownford.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brownford.model.Grade;
import com.brownford.model.User;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(User student);
}