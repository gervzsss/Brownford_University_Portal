package com.brownford.service;

import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Curriculum.Status;
import com.brownford.repository.CurriculumRepository;
import com.brownford.repository.CurriculumCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurriculumService {
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private CurriculumCourseRepository curriculumCourseRepository;

    public List<Curriculum> getAllCurriculums() {
        return curriculumRepository.findAll();
    }

    public Optional<Curriculum> getCurriculumById(Long id) {
        return curriculumRepository.findById(id);
    }

    public Curriculum saveCurriculum(Curriculum curriculum) {
        return curriculumRepository.save(curriculum);
    }

    public void deleteCurriculum(Long id) {
        curriculumRepository.deleteById(id);
    }

    public List<CurriculumCourse> getCoursesForCurriculum(Long curriculumId) {
        return curriculumCourseRepository.findAll().stream()
            .filter(cc -> cc.getCurriculum().getId().equals(curriculumId))
            .toList();
    }

    public CurriculumCourse saveCurriculumCourse(CurriculumCourse curriculumCourse) {
        return curriculumCourseRepository.save(curriculumCourse);
    }

    public void deleteCurriculumCourse(Long id) {
        curriculumCourseRepository.deleteById(id);
    }

    // New methods for versioning and management
    public List<Curriculum> getCurriculumsByProgramId(Long programId) {
        return curriculumRepository.findByProgramId(programId);
    }

    public List<Curriculum> getCurriculumsByProgramIdAndStatus(Long programId, Status status) {
        return curriculumRepository.findByProgramIdAndStatus(programId, status);
    }

    public Curriculum getCurriculumByProgramIdAndYear(Long programId, int yearEffective) {
        return curriculumRepository.findByProgramIdAndYearEffective(programId, yearEffective);
    }

    public Optional<Curriculum> getActiveCurriculumByProgramId(Long programId) {
        // Prefer ACTIVE, fallback to latest by yearEffective if none is ACTIVE
        List<Curriculum> curriculums = curriculumRepository.findByProgramId(programId);
        if (curriculums == null || curriculums.isEmpty()) return Optional.empty();
        return curriculums.stream()
            .filter(c -> c.getStatus() != null && c.getStatus().toString().equalsIgnoreCase("ACTIVE"))
            .findFirst()
            .or(() -> curriculums.stream().max((a, b) -> Integer.compare(a.getYearEffective(), b.getYearEffective())));
    }

    public List<CurriculumCourse> getAllCurriculumCourses() {
        return curriculumCourseRepository.findAll();
    }

    public Optional<CurriculumCourse> getCurriculumCourseById(Long id) {
        return curriculumCourseRepository.findById(id);
    }
}
