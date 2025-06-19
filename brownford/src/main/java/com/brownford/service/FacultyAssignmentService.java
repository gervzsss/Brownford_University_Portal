package com.brownford.service;

import com.brownford.dto.FacultyAssignmentWithScheduleDTO;
import com.brownford.model.FacultyAssignment;
import com.brownford.model.Schedule;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Section;
import com.brownford.model.Faculty;
import com.brownford.repository.FacultyAssignmentRepository;
import com.brownford.repository.ScheduleRepository;
import com.brownford.repository.CurriculumCourseRepository;
import com.brownford.repository.SectionRepository;
import com.brownford.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyAssignmentService {
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private CurriculumCourseRepository curriculumCourseRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    public List<FacultyAssignment> getAllFacultyAssignments() {
        return facultyAssignmentRepository.findAll();
    }

    public Optional<FacultyAssignment> getFacultyAssignmentById(Long id) {
        return facultyAssignmentRepository.findById(id);
    }

    public FacultyAssignment saveFacultyAssignment(FacultyAssignment assignment) {
        return facultyAssignmentRepository.save(assignment);
    }

    public void deleteFacultyAssignment(Long id) {
        facultyAssignmentRepository.deleteById(id);
    }

    @Transactional
    public FacultyAssignment saveFacultyAssignmentWithSchedule(FacultyAssignmentWithScheduleDTO dto) {
        FacultyAssignment assignment = null;
        CurriculumCourse cc = curriculumCourseRepository.findById(dto.getCurriculumCourseId()).orElseThrow();
        Section section = sectionRepository.findById(dto.getSectionId()).orElseThrow();
        Faculty faculty = null;
        if (dto.getFacultyId() != null) {
            faculty = facultyRepository.findById(dto.getFacultyId()).orElse(null);
        }
        boolean isUpdate = (dto.getId() != null);
        if (isUpdate) {
            assignment = facultyAssignmentRepository.findById(dto.getId()).orElse(null);
        }
        if (assignment == null) {
            // Check for existing assignment for this course/section/semester/yearLevel
            assignment = facultyAssignmentRepository
                    .findByCurriculumCourseAndSectionAndSemesterAndYearLevel(
                            cc, section, dto.getSemester(), dto.getYearLevel() != null ? dto.getYearLevel() : 0)
                    .orElse(null);
        }
        if (assignment == null) {
            assignment = new FacultyAssignment();
        }
        assignment.setCurriculumCourse(cc);
        assignment.setSection(section);
        assignment.setFaculty(faculty);
        assignment.setSemester(dto.getSemester());
        assignment.setYearLevel(dto.getYearLevel() != null ? dto.getYearLevel() : 0);
        assignment.setSchoolYear(dto.getSchoolYear());
        FacultyAssignment savedAssignment = facultyAssignmentRepository.save(assignment);
        // Save or update schedule
        Schedule schedule = scheduleRepository.findByCurriculumCourseAndSection(cc, section).stream().findFirst()
                .orElse(new Schedule());
        schedule.setCurriculumCourse(cc);
        schedule.setSection(section);
        schedule.setDay(dto.getDay());
        schedule.setStartTime(java.time.LocalTime.parse(dto.getStartTime()));
        schedule.setEndTime(java.time.LocalTime.parse(dto.getEndTime()));
        schedule.setRoom(dto.getRoom());
        scheduleRepository.save(schedule);
        return savedAssignment;
    }
}
