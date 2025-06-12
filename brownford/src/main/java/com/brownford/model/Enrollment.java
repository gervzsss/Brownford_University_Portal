package com.brownford.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enrollments")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "password", "email", "status", "role", "lastLogin",
            "facultyId", "studentId" })
    private User student;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "enrollment_courses", joinColumns = @JoinColumn(name = "enrollment_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "programs" })
    private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Section section;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private String yearLevel;

    @Column(nullable = false)
    private String status; // e.g., PENDING, APPROVED, DECLINED, COMPLETED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
