package com.brownford.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(nullable = true)
    private String midtermGrade; // e.g., 1.00, 1.25, ..., 5.00, INC, DRP

    @Column(nullable = true)
    private String finalsGrade; // e.g., 1.00, 1.25, ..., 5.00, INC, DRP

    @Column(nullable = true)
    private String finalGrade; // Computed or manually set

    private String remarks; // e.g., Excellent, Very Good, Incomplete, Dropped

    @Column(nullable = false)
    private String semester; // e.g., 1st Semester, 2nd Semester, Summer

    @Column(nullable = false)
    private String schoolYear; // e.g., 2024-2025

    private LocalDateTime dateEncoded;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(String midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public String getFinalsGrade() {
        return finalsGrade;
    }

    public void setFinalsGrade(String finalsGrade) {
        this.finalsGrade = finalsGrade;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public LocalDateTime getDateEncoded() {
        return dateEncoded;
    }

    public void setDateEncoded(LocalDateTime dateEncoded) {
        this.dateEncoded = dateEncoded;
    }
}
