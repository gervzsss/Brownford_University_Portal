package com.brownford.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "curriculums")
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(nullable = false)
    private int yearEffective;

    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<CurriculumCourse> curriculumCourses;

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

    public List<CurriculumCourse> getCurriculumCourses() {
        return curriculumCourses;
    }

    public void setCurriculumCourses(List<CurriculumCourse> curriculumCourses) {
        this.curriculumCourses = curriculumCourses;
    }
}
