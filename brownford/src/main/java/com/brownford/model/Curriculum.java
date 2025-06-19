package com.brownford.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(
    name = "curriculums",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"program_id", "yearEffective"})}
)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Program program;

    @Column(nullable = false)
    private int yearEffective;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<CurriculumCourse> curriculumCourses;

    public enum Status {
        ACTIVE,
        RETIRED
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public int getYearEffective() {
        return yearEffective;
    }

    public void setYearEffective(int yearEffective) {
        this.yearEffective = yearEffective;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<CurriculumCourse> getCurriculumCourses() {
        return curriculumCourses;
    }

    public void setCurriculumCourses(List<CurriculumCourse> curriculumCourses) {
        this.curriculumCourses = curriculumCourses;
    }
}
