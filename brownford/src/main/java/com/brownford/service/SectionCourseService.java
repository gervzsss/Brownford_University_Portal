package com.brownford.service;

import com.brownford.model.SectionCourse;
import com.brownford.repository.SectionCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionCourseService {
    @Autowired
    private SectionCourseRepository sectionCourseRepository;

    public List<SectionCourse> getAllSectionCourses() {
        return sectionCourseRepository.findAll();
    }

    public Optional<SectionCourse> getSectionCourseById(Long id) {
        return sectionCourseRepository.findById(id);
    }

    public SectionCourse saveSectionCourse(SectionCourse sectionCourse) {
        return sectionCourseRepository.save(sectionCourse);
    }

    public void deleteSectionCourse(Long id) {
        sectionCourseRepository.deleteById(id);
    }

    public List<SectionCourse> getSectionCoursesBySectionId(Long sectionId) {
        return sectionCourseRepository.findAll().stream()
            .filter(sc -> sc.getSection().getId().equals(sectionId))
            .toList();
    }
}
