package com.brownford.service;

import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
// import com.brownford.model.Program;
import com.brownford.repository.CurriculumRepository;
import com.brownford.repository.CurriculumCourseRepository;
// import com.brownford.repository.ProgramRepository;
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
    @Autowired
    // private ProgramRepository programRepository;

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

    public Optional<Curriculum> getCurriculumByProgramId(Long programId) {
        return curriculumRepository.findAll().stream()
            .filter(c -> c.getProgram().getId().equals(programId))
            .findFirst();
    }
}
