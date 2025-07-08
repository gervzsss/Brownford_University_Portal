package com.brownford.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.brownford.repository.ScheduleRepository;
import com.brownford.model.Schedule;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.SectionRepository;
import com.brownford.model.Course;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.TextStyle;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private CourseRepository courseRepository;

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
        int pendingGrades = 0;
        java.util.List<Map<String, String>> todaysSchedule = new java.util.ArrayList<>();
        if (principal != null) {
            String username = principal.getName();
            // Find faculty assignments for this faculty
            List<FacultyAssignment> assignments = facultyAssignmentRepository.findAll().stream()
                    .filter(a -> a.getFaculty() != null && a.getFaculty().getUser() != null
                            && a.getFaculty().getUser().getUsername().equals(username))
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
                    List<Schedule> schedules = scheduleRepository
                            .findByCurriculumCourseAndSection(assignment.getCurriculumCourse(), section);
                    for (Schedule sched : schedules) {
                        if (sched.getStartTime() != null && sched.getEndTime() != null) {
                            // Teaching hours
                            java.time.Duration duration = java.time.Duration.between(sched.getStartTime(),
                                    sched.getEndTime());
                            double hours = duration.toMinutes() / 60.0;
                            if (hours > 0) {
                                teachingHours += hours;
                            }
                            // Today's schedule
                            if (sched.getDay() != null
                                    && sched.getDay().toUpperCase().contains(today.substring(0, 1))) {
                                Map<String, String> schedMap = new java.util.HashMap<>();
                                String time = sched.getStartTime().toString() + " - " + sched.getEndTime().toString();
                                String courseTitle = assignment.getCurriculumCourse().getCourse().getCourseTitle();
                                String sectionRoom = section.getSectionCode() + " | "
                                        + (sched.getRoom() != null ? sched.getRoom() : "");
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
        model.addAttribute("teachingHours", (int) Math.round(teachingHours));
        model.addAttribute("pendingGrades", pendingGrades); // Placeholder
        model.addAttribute("todaysSchedule", todaysSchedule);
        return "/faculty/faculty-dashboard";
    }

    @GetMapping("/faculty-workload")
    public String facultyWorkload(
            @RequestParam(value = "schoolYear", required = false) String schoolYear,
            @RequestParam(value = "semester", required = false) String semester,
            Model model, Principal principal) {
        addFacultyToModel(model, principal);
        java.util.List<Map<String, Object>> workloadList = new java.util.ArrayList<>();
        String sy = schoolYear;
        String sem = semester;
        // If not provided, use latest available for this faculty
        if (principal != null) {
            String username = principal.getName();
            java.util.Optional<com.brownford.model.Faculty> facultyOpt = userRepository.findByUsername(username)
                    .flatMap(user -> facultyRepository.findByUser(user));
            if (facultyOpt.isPresent()) {
                com.brownford.model.Faculty faculty = facultyOpt.get();
                java.util.List<FacultyAssignment> assignments = facultyAssignmentRepository.findAll().stream()
                        .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                        .collect(Collectors.toList());
                // Get all unique school years and semesters
                java.util.List<String> schoolYears = assignments.stream().map(a -> a.getSchoolYear()).distinct()
                        .sorted((a, b) -> b.compareTo(a)).collect(java.util.stream.Collectors.toList());
                java.util.List<String> semesters = assignments.stream().map(a -> a.getSemester())
                        .filter(s -> s.equals("First") || s.equals("Second")).distinct().sorted()
                        .collect(java.util.stream.Collectors.toList());
                // Default to latest school year and first semester if not provided
                if (sy == null && !schoolYears.isEmpty())
                    sy = schoolYears.get(0);
                if (sem == null && !semesters.isEmpty())
                    sem = semesters.get(0);
                final String selectedSy = sy;
                final String selectedSem = sem;
                // Filter assignments by selected school year and semester
                assignments = assignments.stream()
                        .filter(a -> (selectedSy == null || a.getSchoolYear().equals(selectedSy))
                                && (selectedSem == null || a.getSemester().equals(selectedSem)))
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
                        java.util.List<Schedule> schedules = scheduleRepository
                                .findByCurriculumCourseAndSection(assignment.getCurriculumCourse(), section);
                        for (Schedule sched : schedules) {
                            if (scheduleStr.length() > 0)
                                scheduleStr.append("/");
                            scheduleStr.append((sched.getDay() != null ? sched.getDay() : "") + " " +
                                    (sched.getStartTime() != null ? sched.getStartTime().toString() : "") + "-" +
                                    (sched.getEndTime() != null ? sched.getEndTime().toString() : ""));
                            if (roomStr.length() > 0)
                                roomStr.append("/");
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
                // Add filter options to model for frontend
                model.addAttribute("schoolYears", schoolYears);
                model.addAttribute("semesters", semesters);
                model.addAttribute("selectedSchoolYear", sy);
                model.addAttribute("selectedSemester", sem);
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
    public String facultySchedule(
            @RequestParam(value = "schoolYear", required = false) String schoolYear,
            @RequestParam(value = "semester", required = false) String semester,
            Model model, Principal principal) {
        addFacultyToModel(model, principal);
        final String sy = (schoolYear == null) ? "2425" : schoolYear;
        final String sem = (semester == null) ? "Second" : semester;
        model.addAttribute("schoolYear", sy);
        model.addAttribute("semester", sem);

        // Days for the table
        String[] days = { "M", "T", "W", "TH", "F", "S" };
        model.addAttribute("days", days);

        // Gather all schedules for this faculty, school year, and semester
        java.util.Set<String> timeSlotSet = new java.util.TreeSet<>();
        Map<String, Map<String, java.util.List<Map<String, String>>>> weeklySchedule = new HashMap<>();
        for (String day : days) {
            weeklySchedule.put(day, new HashMap<>());
        }

        if (principal != null) {
            String username = principal.getName();
            var facultyOpt = userRepository.findByUsername(username)
                    .flatMap(user -> facultyRepository.findByUser(user));
            if (facultyOpt.isPresent()) {
                var faculty = facultyOpt.get();
                var assignments = facultyAssignmentRepository.findAll().stream()
                        .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId())
                                && a.getSchoolYear().equals(sy)
                                && a.getSemester().equalsIgnoreCase(sem))
                        .collect(Collectors.toList());
                // Map full day names to short codes
                Map<String, String> dayMap = Map.of(
                        "Monday", "M",
                        "Tuesday", "T",
                        "Wednesday", "W",
                        "Thursday", "TH",
                        "Friday", "F",
                        "Saturday", "S",
                        "Sunday", "SU");
                for (var assignment : assignments) {
                    var section = assignment.getSection();
                    var curriculumCourse = assignment.getCurriculumCourse();
                    var course = curriculumCourse.getCourse();
                    var schedules = scheduleRepository.findByCurriculumCourseAndSection(curriculumCourse, section);
                    for (var sched : schedules) {
                        String schedDayRaw = sched.getDay();
                        String schedDay = dayMap.getOrDefault(schedDayRaw, schedDayRaw); // fallback to original if not
                                                                                         // mapped
                        String schedTime = String.format("%02d:%02d-%02d:%02d",
                                sched.getStartTime().getHour(), sched.getStartTime().getMinute(),
                                sched.getEndTime().getHour(), sched.getEndTime().getMinute());
                        timeSlotSet.add(schedTime);
                        Map<String, String> entry = new HashMap<>();
                        entry.put("course", course.getCourseCode());
                        entry.put("title", course.getCourseTitle());
                        entry.put("section", section.getSectionCode());
                        entry.put("room", sched.getRoom() != null ? sched.getRoom() : "");
                        entry.put("time", schedTime);
                        if (weeklySchedule.containsKey(schedDay)) {
                            weeklySchedule.get(schedDay).computeIfAbsent(schedTime, k -> new java.util.ArrayList<>())
                                    .add(entry);
                        } else {
                            System.out.println("[FacultySchedule] Unknown day in schedule: " + schedDayRaw);
                        }
                    }
                }
            }
        }
        // Convert set to sorted list for table rows
        java.util.List<String> timeSlots = new java.util.ArrayList<>(timeSlotSet);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("weeklySchedule", weeklySchedule);
        return "/faculty/faculty-schedule";
    }

    @GetMapping("/faculty-profile")
    public String facultyProfile(Model model, Principal principal) {
        addFacultyToModel(model, principal);
        return "/faculty/faculty-profile";
    }

    @GetMapping("/faculty-grading-sheet")
    public String facultyGradingSheet(
            @RequestParam("section") String sectionCode,
            @RequestParam("course") String courseCode,
            Model model,
            Principal principal) {
        addFacultyToModel(model, principal);
        Section section = sectionRepository.findBySectionCode(sectionCode).orElse(null);
        Course course = courseRepository.findByCourseCode(courseCode).orElse(null);
        List<Student> students = new java.util.ArrayList<>();
        if (section != null && course != null) {
            List<Enrollment> enrollments = enrollmentRepository.findAll().stream()
                    .filter(e -> "APPROVED".equalsIgnoreCase(e.getStatus())
                            && e.getSection() != null && e.getSection().getId().equals(section.getId())
                            && e.getCourses() != null
                            && e.getCourses().stream().anyMatch(c -> c.getId().equals(course.getId())))
                    .collect(Collectors.toList());
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudent() != null) {
                    students.add(enrollment.getStudent());
                }
            }
        }
        // Find the FacultyAssignment for this section and course
        FacultyAssignment assignment = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getSection() != null && a.getSection().getSectionCode().equals(sectionCode)
                        && a.getCurriculumCourse() != null
                        && a.getCurriculumCourse().getCourse().getCourseCode().equals(courseCode))
                .findFirst().orElse(null);
        String semester = assignment != null ? assignment.getSemester() : "";
        String schoolYear = assignment != null ? assignment.getSchoolYear() : "";
        // --- Schedule string formatting ---
        String scheduleString = "";
        if (assignment != null && assignment.getCurriculumCourse() != null && section != null) {
            List<Schedule> schedules = scheduleRepository
                    .findByCurriculumCourseAndSection(assignment.getCurriculumCourse(), section);
            scheduleString = schedules.stream()
                    .map(s -> (s.getDay() != null ? s.getDay() : "") + " " +
                            (s.getStartTime() != null ? s.getStartTime().toString() : "") + "-" +
                            (s.getEndTime() != null ? s.getEndTime().toString() : ""))
                    .collect(Collectors.joining("/"));
        }
        model.addAttribute("students", students);
        model.addAttribute("section", section);
        model.addAttribute("course", course);
        model.addAttribute("semester", semester);
        model.addAttribute("schoolYear", schoolYear);
        model.addAttribute("scheduleString", scheduleString);
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

@RestController
@RequestMapping("/api/faculty")
class FacultyScheduleRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;

    @GetMapping("/schedule-filters")
    public Map<String, Object> getScheduleFilters(Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null)
            return result;
        String username = principal.getName();
        var facultyOpt = userRepository.findByUsername(username)
                .flatMap(user -> facultyRepository.findByUser(user));
        if (facultyOpt.isEmpty())
            return result;
        var faculty = facultyOpt.get();
        var assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                .collect(Collectors.toList());
        // Unique school years, semesters, year levels
        var schoolYears = assignments.stream().map(a -> a.getSchoolYear()).distinct().sorted()
                .collect(Collectors.toList());
        var semesters = assignments.stream().map(a -> a.getSemester()).distinct().sorted().collect(Collectors.toList());
        var yearLevels = assignments.stream().map(a -> a.getYearLevel()).distinct().sorted()
                .collect(Collectors.toList());
        result.put("schoolYears", schoolYears);
        result.put("semesters", semesters);
        result.put("yearLevels", yearLevels);
        return result;
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyScheduleDataRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/schedule-data")
    public Map<String, Object> getScheduleData(
            @RequestParam(value = "schoolYear", required = false) String schoolYear,
            @RequestParam(value = "semester", required = false) String semester,
            Principal principal) {
        Map<String, Object> result = new HashMap<>();
        final String sy = (schoolYear == null) ? "2425" : schoolYear;
        final String sem = (semester == null) ? "Second" : semester;
        String[] days = { "M", "T", "W", "TH", "F", "S" };
        result.put("days", days);
        java.util.Set<String> timeSlotSet = new java.util.TreeSet<>();
        Map<String, Map<String, java.util.List<Map<String, String>>>> weeklySchedule = new HashMap<>();
        for (String day : days) {
            weeklySchedule.put(day, new HashMap<>());
        }
        if (principal != null) {
            String username = principal.getName();
            var facultyOpt = userRepository.findByUsername(username)
                    .flatMap(user -> facultyRepository.findByUser(user));
            if (facultyOpt.isPresent()) {
                var faculty = facultyOpt.get();
                var assignments = facultyAssignmentRepository.findAll().stream()
                        .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId())
                                && a.getSchoolYear().equals(sy)
                                && a.getSemester().equalsIgnoreCase(sem))
                        .collect(Collectors.toList());
                Map<String, String> dayMap = Map.of(
                        "Monday", "M",
                        "Tuesday", "T",
                        "Wednesday", "W",
                        "Thursday", "TH",
                        "Friday", "F",
                        "Saturday", "S",
                        "Sunday", "SU");
                for (var assignment : assignments) {
                    var section = assignment.getSection();
                    var curriculumCourse = assignment.getCurriculumCourse();
                    var course = curriculumCourse.getCourse();
                    var schedules = scheduleRepository.findByCurriculumCourseAndSection(curriculumCourse, section);
                    for (var sched : schedules) {
                        String schedDayRaw = sched.getDay();
                        String schedDay = dayMap.getOrDefault(schedDayRaw, schedDayRaw);
                        String schedTime = String.format("%02d:%02d-%02d:%02d",
                                sched.getStartTime().getHour(), sched.getStartTime().getMinute(),
                                sched.getEndTime().getHour(), sched.getEndTime().getMinute());
                        timeSlotSet.add(schedTime);
                        Map<String, String> entry = new HashMap<>();
                        entry.put("course", course.getCourseCode());
                        entry.put("title", course.getCourseTitle());
                        entry.put("section", section.getSectionCode());
                        entry.put("room", sched.getRoom() != null ? sched.getRoom() : "");
                        entry.put("time", schedTime);
                        if (weeklySchedule.containsKey(schedDay)) {
                            weeklySchedule.get(schedDay).computeIfAbsent(schedTime, k -> new java.util.ArrayList<>())
                                    .add(entry);
                        }
                    }
                }
            }
        }
        java.util.List<String> timeSlots = new java.util.ArrayList<>(timeSlotSet);
        result.put("timeSlots", timeSlots);
        result.put("weeklySchedule", weeklySchedule);
        return result;
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyWorkloadRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;

    @GetMapping("/workload-filters")
    public Map<String, Object> getWorkloadFilters(Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null)
            return result;
        String username = principal.getName();
        var facultyOpt = userRepository.findByUsername(username)
                .flatMap(user -> facultyRepository.findByUser(user));
        if (facultyOpt.isEmpty())
            return result;
        var faculty = facultyOpt.get();
        var assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                .collect(Collectors.toList());
        // Unique school years and semesters
        var schoolYears = assignments.stream().map(a -> a.getSchoolYear()).distinct().sorted()
                .collect(Collectors.toList());
        var semesters = assignments.stream().map(a -> a.getSemester()).distinct().sorted().collect(Collectors.toList());
        // Format for frontend [{value, label}]
        result.put("schoolYears",
                schoolYears.stream().map(y -> Map.of("value", y, "label", y)).collect(Collectors.toList()));
        result.put("semesters", semesters.stream().filter(s -> !"Summer".equalsIgnoreCase(s))
                .map(s -> Map.of("value", s, "label", s)).collect(Collectors.toList()));
        return result;
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyWorkloadDataRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/workload-data")
    public List<Map<String, Object>> getWorkloadData(
            @RequestParam(value = "schoolYear", required = false) String schoolYear,
            @RequestParam(value = "semester", required = false) String semester,
            Principal principal) {
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        if (principal == null)
            return result;
        String username = principal.getName();
        var facultyOpt = userRepository.findByUsername(username)
                .flatMap(user -> facultyRepository.findByUser(user));
        if (facultyOpt.isEmpty())
            return result;
        var faculty = facultyOpt.get();
        var assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                .filter(a -> (schoolYear == null || a.getSchoolYear().equals(schoolYear)))
                .filter(a -> (semester == null || a.getSemester().equalsIgnoreCase(semester)))
                .collect(Collectors.toList());
        int no = 1;
        for (var assignment : assignments) {
            var section = assignment.getSection();
            var curriculumCourse = assignment.getCurriculumCourse();
            var course = curriculumCourse.getCourse();
            var schedules = scheduleRepository.findByCurriculumCourseAndSection(curriculumCourse, section);
            StringBuilder schedStr = new StringBuilder();
            for (var sched : schedules) {
                if (schedStr.length() > 0)
                    schedStr.append(", ");
                schedStr.append(sched.getDay()).append(" ")
                        .append(String.format("%02d:%02d-%02d:%02d",
                                sched.getStartTime().getHour(), sched.getStartTime().getMinute(),
                                sched.getEndTime().getHour(), sched.getEndTime().getMinute()));
            }
            Map<String, Object> row = new HashMap<>();
            row.put("no", no++);
            row.put("section", section.getSectionCode());
            row.put("subjectCode", course.getCourseCode());
            row.put("description", course.getCourseTitle());
            row.put("schedule", schedStr.toString());
            row.put("room", schedules.isEmpty() ? "" : schedules.get(0).getRoom());
            row.put("units", course.getUnits());
            row.put("sectionParam", section.getSectionCode());
            row.put("subjectParam", course.getCourseCode());
            result.add(row);
        }
        return result;
    }
}

@RestController
@RequestMapping("/api/faculty/profile")
class FacultyProfileRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public Map<String, Object> getProfileInfo(Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null)
            return result;
        String username = principal.getName();
        Faculty faculty = facultyRepository.findAll().stream().filter(s -> s.getUser().getUsername().equals(username))
                .findFirst().orElse(null);
        if (faculty == null)
            return result;
        result.put("facultyId", faculty.getFacultyId());
        result.put("firstName", faculty.getUser().getFirstName());
        result.put("middleName", faculty.getUser().getMiddleName());
        result.put("lastName", faculty.getUser().getLastName());
        result.put("email", faculty.getUser().getEmail());
        result.put("gender", faculty.getGender());
        result.put("dateOfBirth", faculty.getDateOfBirth());
        result.put("mobileNumber", faculty.getMobileNumber());
        result.put("address", faculty.getAddress());
        return result;
    }

    @PutMapping("/info")
    public Map<String, Object> updateProfileInfo(@RequestBody Map<String, Object> payload, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("success", false);
            return result;
        }
        String username = principal.getName();
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            return result;
        }
        var user = userOpt.get();
        var facultyOpt = facultyRepository.findByUser(user);
        if (facultyOpt.isEmpty()) {
            result.put("success", false);
            return result;
        }
        var faculty = facultyOpt.get();
        // Update fields
        if (payload.containsKey("gender"))
            faculty.setGender((String) payload.get("gender"));
        if (payload.containsKey("dateOfBirth"))
            faculty.setDateOfBirth((String) payload.get("dateOfBirth"));
        if (payload.containsKey("mobileNumber"))
            faculty.setMobileNumber((String) payload.get("mobileNumber"));
        if (payload.containsKey("address"))
            faculty.setAddress((String) payload.get("address"));
        facultyRepository.save(faculty);
        result.put("success", true);
        return result;
    }

    @PostMapping("/change-password")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> payload, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("success", false);
            result.put("message", "Not authenticated");
            return result;
        }
        String username = principal.getName();
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            result.put("success", false);
            result.put("message", "User not found");
            return result;
        }
        var user = userOpt.get();
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            result.put("success", false);
            result.put("message", "Current password is incorrect.");
            return result;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        result.put("success", true);
        return result;
    }
}

@RestController
@RequestMapping("/api/faculty")
class FacultyClassListRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyAssignmentRepository facultyAssignmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/class-list")
    public List<Map<String, Object>> getFacultyClassList(Principal principal) {
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        if (principal == null)
            return result;
        String username = principal.getName();
        var facultyOpt = userRepository.findByUsername(username)
                .flatMap(user -> facultyRepository.findByUser(user));
        if (facultyOpt.isEmpty())
            return result;
        var faculty = facultyOpt.get();
        var assignments = facultyAssignmentRepository.findAll().stream()
                .filter(a -> a.getFaculty() != null && a.getFaculty().getId().equals(faculty.getId()))
                .collect(Collectors.toList());
        for (var assignment : assignments) {
            var section = assignment.getSection();
            var curriculumCourse = assignment.getCurriculumCourse();
            if (section == null || curriculumCourse == null)
                continue;
            var course = curriculumCourse.getCourse();
            // Schedule string
            var schedules = scheduleRepository.findByCurriculumCourseAndSection(curriculumCourse, section);
            StringBuilder scheduleStr = new StringBuilder();
            StringBuilder roomStr = new StringBuilder();
            for (var sched : schedules) {
                if (scheduleStr.length() > 0)
                    scheduleStr.append(" / ");
                scheduleStr.append((sched.getDay() != null ? sched.getDay() : "") + " " +
                        (sched.getStartTime() != null ? sched.getStartTime().toString() : "") + "-" +
                        (sched.getEndTime() != null ? sched.getEndTime().toString() : ""));
                if (roomStr.length() > 0)
                    roomStr.append(" / ");
                roomStr.append(sched.getRoom() != null ? sched.getRoom() : "");
            }
            // Enrolled students
            var enrollments = enrollmentRepository.findAll().stream()
                    .filter(e -> e.getSection() != null && e.getSection().getId().equals(section.getId())
                            && e.getCourses() != null
                            && e.getCourses().stream().anyMatch(c -> c.getId().equals(course.getId()))
                            && "APPROVED".equals(e.getStatus()))
                    .collect(Collectors.toList());
            List<Map<String, Object>> students = new java.util.ArrayList<>();
            int no = 1;
            for (var enrollment : enrollments) {
                var student = enrollment.getStudent();
                if (student == null)
                    continue;
                var user = student.getUser();
                Map<String, Object> studentMap = new HashMap<>();
                studentMap.put("no", no++);
                studentMap.put("studentNo", student.getStudentId());
                studentMap.put("fullName",
                        (user.getLastName() + ", " + user.getFirstName() + " " + user.getMiddleName()).toUpperCase());
                studentMap.put("email", user.getEmail());
                studentMap.put("cellphoneNumber", student.getMobileNumber()); // Add cellphone number
                students.add(studentMap);
            }
            Map<String, Object> classMap = new HashMap<>();
            classMap.put("sectionCode", section.getSectionCode());
            classMap.put("courseCode", course.getCourseCode());
            classMap.put("courseTitle", course.getCourseTitle());
            classMap.put("schoolYear", assignment.getSchoolYear());
            classMap.put("semester", assignment.getSemester());
            classMap.put("schedule", scheduleStr.toString());
            classMap.put("room", roomStr.toString());
            classMap.put("students", students);
            result.add(classMap);
        }
        return result;
    }
}