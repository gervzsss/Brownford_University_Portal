package com.brownford.model;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty_assignment")
public class FacultyAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curriculum_course_id", nullable = false)
    private CurriculumCourse curriculumCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Faculty faculty;

    @Column(nullable = false)
    private String semester;

    @Column(nullable = false)
    private int yearLevel;

    @Column(name = "school_year", nullable = false)
    private String schoolYear;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurriculumCourse getCurriculumCourse() {
        return curriculumCourse;
    }

    public void setCurriculumCourse(CurriculumCourse curriculumCourse) {
        this.curriculumCourse = curriculumCourse;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }
}
