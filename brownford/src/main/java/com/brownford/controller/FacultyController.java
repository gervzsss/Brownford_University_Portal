package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;

import com.brownford.repository.FacultyRepository;
import com.brownford.repository.UserRepository;
import com.brownford.model.User;
import com.brownford.repository.FacultyAssignmentRepository;
import com.brownford.repository.EnrollmentRepository;
import com.brownford.model.FacultyAssignment;
import com.brownford.model.Enrollment;
import com.brownford.model.Faculty;
import com.brownford.model.Section;
import com.brownford.model.Student;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import com.brownford.repository.ScheduleRepository;
import com.brownford.model.Schedule;
import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
public class FacultyController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    private void addFacultyToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User facultyUser = userRepository.findByUsername(username).orElse(null);
            model.addAttribute("facultyUser", facultyUser);
            if (facultyUser != null) {
                facultyRepository.findByUser(facultyUser).ifPresent(faculty -> model.addAttribute("faculty", faculty));
            }
        }
    }

    @GetMapping("/faculty-dashboard")
    public String facultyDashboard(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        int totalStudents = 0;
        double teachingHours = 0.0;
        int pendingGrades = 0; // TODO: Integrate with Grade backend
        java.util.List<Map<String, String>> todaysSchedule = new java.util.ArrayList<>();
        if (principal != null) {
            String username = principal.getName();
            // Find faculty assignments for this faculty
            List<FacultyAssignment> assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getUser() != null && a.getFaculty().getUser().getUsername().equals(username))
                .collect(java.util.stream.Collectors.toList());
            java.util.Set<Long> uniqueStudentIds = new java.util.HashSet<>();
            // Get today's day abbreviation (e.g., MONDAY -> M, TUESDAY -> T, etc.)
            String today = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
            // Map Java day abbreviations to your DB format if needed
            for (FacultyAssignment assignment : assignments) {
                Section section = assignment.getSection();
                String semester = assignment.getSemester();
                if (section != null && semester != null) {
                    List<Enrollment> enrollments = enrollmentRepository.findAll().stream()
                        .filter(e -> "APPROVED".equalsIgnoreCase(e.getStatus())
                            && e.getSection() != null && e.getSection().getId().equals(section.getId())
                            && semester.equalsIgnoreCase(e.getSemester()))
                        .collect(java.util.stream.Collectors.toList());
                    for (Enrollment enrollment : enrollments) {
                        Student student = enrollment.getStudent();
                        if (student != null && student.getId() != null) {
                            uniqueStudentIds.add(student.getId());
                        }
                    }
                }
                // Calculate teaching hours for this assignment
                if (assignment.getCurriculumCourse() != null && section != null) {
                    List<Schedule> schedules = scheduleRepository.findByCurriculumCourseAndSection(assignment.getCurriculumCourse(), section);
                    for (Schedule sched : schedules) {
                        if (sched.getStartTime() != null && sched.getEndTime() != null) {
                            // Teaching hours
                            java.time.Duration duration = java.time.Duration.between(sched.getStartTime(), sched.getEndTime());
                            double hours = duration.toMinutes() / 60.0;
                            if (hours > 0) {
                                teachingHours += hours;
                            }
                            // Today's schedule
                            if (sched.getDay() != null && sched.getDay().toUpperCase().contains(today.substring(0,1))) {
                                Map<String, String> schedMap = new java.util.HashMap<>();
                                String time = sched.getStartTime().toString() + " - " + sched.getEndTime().toString();
                                String courseTitle = assignment.getCurriculumCourse().getCourse().getCourseTitle();
                                String sectionRoom = section.getSectionCode() + " | " + (sched.getRoom() != null ? sched.getRoom() : "");
                                // Status
                                LocalTime now = LocalTime.now();
                                String status = "Upcoming";
                                if (now.isAfter(sched.getStartTime()) && now.isBefore(sched.getEndTime())) {
                                    status = "Ongoing";
                                } else if (now.isAfter(sched.getEndTime())) {
                                    status = "Completed";
                                }
                                schedMap.put("time", time);
                                schedMap.put("courseTitle", courseTitle);
                                schedMap.put("sectionRoom", sectionRoom);
                                schedMap.put("status", status);
                                todaysSchedule.add(schedMap);
                            }
                        }
                    }
                }
            }
            totalStudents = uniqueStudentIds.size();
        }
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("teachingHours", (int)Math.round(teachingHours));
        model.addAttribute("pendingGrades", pendingGrades); // Placeholder
        model.addAttribute("todaysSchedule", todaysSchedule);
        return "/faculty/faculty-dashboard";
    }

    @GetMapping("/faculty-workload")
    public String facultyWorkload(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        java.util.List<Map<String, Object>> workloadList = new java.util.ArrayList<>();
        if (principal != null) {
            String username = principal.getName();
            List<FacultyAssignment> assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getUser() != null && a.getFaculty().getUser().getUsername().equals(username))
                .collect(java.util.stream.Collectors.toList());
            int rowNum = 1;
            for (FacultyAssignment assignment : assignments) {
                Map<String, Object> row = new java.util.HashMap<>();
                row.put("no", rowNum++);
                Section section = assignment.getSection();
                row.put("section", section != null ? section.getSectionCode() : "");
                if (assignment.getCurriculumCourse() != null) {
                    row.put("subjectCode", assignment.getCurriculumCourse().getCourse().getCourseCode());
                    row.put("description", assignment.getCurriculumCourse().getCourse().getCourseTitle());
                    row.put("units", assignment.getCurriculumCourse().getCourse().getUnits());
                } else {
                    row.put("subjectCode", "");
                    row.put("description", "");
                    row.put("units", "");
                }
                // Gather all schedules for this assignment
                StringBuilder scheduleStr = new StringBuilder();
                StringBuilder roomStr = new StringBuilder();
                if (assignment.getCurriculumCourse() != null && section != null) {
                    java.util.List<Schedule> schedules = scheduleRepository.findByCurriculumCourseAndSection(assignment.getCurriculumCourse(), section);
                    for (Schedule sched : schedules) {
                        if (scheduleStr.length() > 0) scheduleStr.append("/");
                        scheduleStr.append((sched.getDay() != null ? sched.getDay() : "") + " " +
                            (sched.getStartTime() != null ? sched.getStartTime().toString() : "") + "-" +
                            (sched.getEndTime() != null ? sched.getEndTime().toString() : ""));
                        if (roomStr.length() > 0) roomStr.append("/");
                        roomStr.append(sched.getRoom() != null ? sched.getRoom() : "");
                    }
                }
                row.put("schedule", scheduleStr.toString());
                row.put("room", roomStr.toString());
                // For actions, pass section and subject code for links
                row.put("sectionParam", section != null ? section.getSectionCode() : "");
                row.put("subjectParam", row.get("subjectCode"));
                workloadList.add(row);
            }
        }
        model.addAttribute("workloadList", workloadList);
        return "/faculty/faculty-workload";
    }

    @GetMapping("/faculty-class-list")
    public String facultyClassList(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-class-list";
    }

    @GetMapping("/faculty-schedule")
    public String facultySchedule(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-schedule";
    }

    @GetMapping("/faculty-profile")
    public String facultyProfile(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-profile";
    }

    @GetMapping("/faculty-grading-sheet")
    public String facultyGradingSheet(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-grading-sheet";
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyRestController {
    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping
    public List<Map<String, Object>> getAllFaculty() {
        return facultyRepository.findAll().stream().map(faculty -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", faculty.getId());
            map.put("facultyId", faculty.getFacultyId());
            map.put("name", faculty.getUser().getFullName());
            return map;
        }).collect(Collectors.toList());
    }
}
