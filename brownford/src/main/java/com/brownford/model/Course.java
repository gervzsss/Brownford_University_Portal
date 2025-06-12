package com.brownford.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "courses")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseCode;

    @Column(nullable = false)
    private String courseTitle;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course_programs", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "program_id"))
    private java.util.List<Program> programs = new java.util.ArrayList<>();

    @Column(nullable = false)
    private int units;

    private String prerequisites;

    @Column(nullable = false)
    private String status;

    private String corequisites;

    @Column(name = "yearLevel", nullable = true)
    private Integer yearLevel; // 1=First Year, 2=Second Year, etc.

    @Column(name = "semester", nullable = true)
    private String semester;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public java.util.List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(java.util.List<Program> programs) {
        this.programs = programs;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCorequisites() {
        return corequisites;
    }

    public void setCorequisites(String corequisites) {
        this.corequisites = corequisites;
    }

    public Integer getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(Integer yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
