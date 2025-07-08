package com.brownford.service;

import com.brownford.dto.FacultyAssignmentWithScheduleDTO;
import com.brownford.model.FacultyAssignment;
import com.brownford.model.Schedule;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Section;
import com.brownford.model.Faculty;
import com.brownford.model.Notification;
import com.brownford.repository.FacultyAssignmentRepository;
import com.brownford.repository.ScheduleRepository;
import com.brownford.repository.CurriculumCourseRepository;
import com.brownford.repository.SectionRepository;
import com.brownford.repository.FacultyRepository;
import com.brownford.repository.NotificationRepository;
import com.brownford.exception.ScheduleConflictException;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyAssignmentService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyAssignmentService.class);

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
    @Autowired
    private NotificationRepository notificationRepository;

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
        Optional<FacultyAssignment> assignmentOpt = facultyAssignmentRepository.findById(id);
        if (assignmentOpt.isPresent()) {
            FacultyAssignment assignment = assignmentOpt.get();
            Faculty faculty = assignment.getFaculty();
            Section section = assignment.getSection();
            CurriculumCourse cc = assignment.getCurriculumCourse();
            logger.info("Attempting to delete assignment: id={}, faculty={}, section={}, course={}", id, faculty != null ? faculty.getId() : null, section != null ? section.getSectionCode() : null, cc != null ? cc.getCourse().getCourseCode() : null);
            facultyAssignmentRepository.deleteById(id);
            // Notify faculty of removal
            if (faculty != null && section != null && cc != null) {
                Notification notif = new Notification();
                notif.setFaculty(faculty);
                notif.setType("REMOVAL");
                notif.setMessage("You have been removed from section " + section.getSectionCode() + " for course " + cc.getCourse().getCourseCode() + ".");
                notif.setCreatedAt(java.time.LocalDateTime.now());
                notif.setRead(false);
                notif.setTargetUrl("/faculty-schedule");
                notificationRepository.save(notif);
                logger.info("Faculty removal notification created for facultyId={}, section={}, course={}", faculty.getId(), section.getSectionCode(), cc.getCourse().getCourseCode());
            } else {
                logger.warn("Faculty, section, or course is null. Notification not created for assignment id={}", id);
            }
        } else {
            facultyAssignmentRepository.deleteById(id);
            logger.warn("Assignment id={} not found for deletion.", id);
        }
    }

    @Transactional
    public FacultyAssignment saveFacultyAssignmentWithSchedule(FacultyAssignmentWithScheduleDTO dto) {
        logger.debug("Incoming FacultyAssignmentWithScheduleDTO: id={}, curriculumCourseId={}, sectionId={}, facultyId={}, semester={}, yearLevel={}, schoolYear={}", dto.getId(), dto.getCurriculumCourseId(), dto.getSectionId(), dto.getFacultyId(), dto.getSemester(), dto.getYearLevel(), dto.getSchoolYear());
        
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
        Faculty previousFaculty = null;
        if (assignment != null) {
            previousFaculty = assignment.getFaculty();
        }
        // --- Notification logic ---
        // Notify previous faculty if unassigned (regardless of update/insert)
        if (previousFaculty != null && faculty == null) {
            Notification notif = new Notification();
            notif.setFaculty(previousFaculty);
            notif.setType("REMOVAL");
            notif.setMessage("You have been unassigned from section " + section.getSectionCode() + " as faculty for course " + cc.getCourse().getCourseCode() + ".");
            notif.setCreatedAt(java.time.LocalDateTime.now());
            notif.setRead(false);
            notif.setTargetUrl("/faculty-schedule");
            notificationRepository.save(notif);
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
        // --- Schedule conflict checks ---
        LocalTime startTime = LocalTime.parse(dto.getStartTime());
        LocalTime endTime = LocalTime.parse(dto.getEndTime());
        String day = dto.getDay();
        String room = dto.getRoom();
        String semester = dto.getSemester();
        String schoolYear = dto.getSchoolYear();
        // Determine if this is an update (existing schedule) or a new schedule
        Schedule existingSchedule = scheduleRepository.findByCurriculumCourseAndSection(cc, section).stream().findFirst().orElse(null);
        Long scheduleId = (existingSchedule != null && existingSchedule.getId() != null) ? existingSchedule.getId() : null;
        // Section conflict
        scheduleRepository.findConflictsBySection(section, day, startTime, endTime, semester, schoolYear).stream()
            .filter(s -> scheduleId == null || !s.getId().equals(scheduleId))
            .findAny()
            .ifPresent(s -> { throw new ScheduleConflictException("Section already has a schedule at this time."); });
        // Faculty conflict
        if (faculty != null) {
            scheduleRepository.findConflictsByFaculty(faculty, day, startTime, endTime, semester, schoolYear).stream()
                .filter(s -> scheduleId == null || !s.getId().equals(scheduleId))
                .findAny()
                .ifPresent(s -> { throw new ScheduleConflictException("Faculty already has a schedule at this time."); });
        }
        // Room conflict
        if (room != null && !room.isEmpty()) {
            scheduleRepository.findConflictsByRoom(room, day, startTime, endTime, semester, schoolYear).stream()
                .filter(s -> scheduleId == null || !s.getId().equals(scheduleId))
                .findAny()
                .ifPresent(s -> { throw new ScheduleConflictException("Room is already booked at this time."); });
        }
        scheduleRepository.save(schedule);
        // Notify new assignment
        if (faculty != null) {
            Notification notif = new Notification();
            notif.setFaculty(faculty);
            notif.setType("ASSIGNMENT");
            notif.setMessage("You have been assigned to section " + section.getSectionCode() + " for course " + cc.getCourse().getCourseCode() + ".");
            notif.setCreatedAt(java.time.LocalDateTime.now());
            notif.setRead(false);
            notif.setTargetUrl("/faculty-schedule");
            notificationRepository.save(notif);
        }
        return savedAssignment;
    }
}
