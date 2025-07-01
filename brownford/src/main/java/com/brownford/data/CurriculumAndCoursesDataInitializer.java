package com.brownford.data;

import com.brownford.model.Curriculum;
import com.brownford.model.CurriculumCourse;
import com.brownford.model.Program;
import com.brownford.model.Course;
import com.brownford.model.Curriculum.Status;
import com.brownford.repository.CurriculumRepository;
import com.brownford.repository.ProgramRepository;
import com.brownford.repository.CourseRepository;
import com.brownford.repository.CurriculumCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@org.springframework.context.annotation.DependsOn({"courseDataInitializer", "programDataInitializer"})
public class CurriculumAndCoursesDataInitializer implements CommandLineRunner {
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CurriculumCourseRepository curriculumCourseRepository;

    @Override
    public void run(String... args) {
        if (curriculumRepository.count() == 0) {
            List<Curriculum> curriculums = new ArrayList<>();
            Map<Long, Curriculum> curriculumMap = new HashMap<>();

            Program p1 = programRepository.findById(1L).orElse(null);
            Program p2 = programRepository.findById(2L).orElse(null);
            Program p3 = programRepository.findById(3L).orElse(null);
            Program p4 = programRepository.findById(4L).orElse(null);
            Program p5 = programRepository.findById(5L).orElse(null);

            if (p1 != null) {
                Curriculum c1 = new Curriculum();
                c1.setYearEffective(2025);
                c1.setProgram(p1);
                c1.setDescription("BSTAT CURRICULUM 2025");
                c1.setStatus(Status.ACTIVE);
                curriculums.add(c1);
            }
            if (p2 != null) {
                Curriculum c2 = new Curriculum();
                c2.setYearEffective(2025);
                c2.setProgram(p2);
                c2.setDescription("BSA CURRICULUM 2025");
                c2.setStatus(Status.ACTIVE);
                curriculums.add(c2);
            }
            if (p3 != null) {
                Curriculum c3 = new Curriculum();
                c3.setYearEffective(2025);
                c3.setProgram(p3);
                c3.setDescription("BSIT CURRICULUM 2025");
                c3.setStatus(Status.ACTIVE);
                curriculums.add(c3);
            }
            if (p4 != null) {
                Curriculum c4 = new Curriculum();
                c4.setYearEffective(2025);
                c4.setProgram(p4);
                c4.setDescription("BSPSYCH CURRICULUM 2025");
                c4.setStatus(Status.ACTIVE);
                curriculums.add(c4);
            }
            if (p5 != null) {
                Curriculum c5 = new Curriculum();
                c5.setYearEffective(2025);
                c5.setProgram(p5);
                c5.setDescription("BSMM CURRICULUM 2025");
                c5.setStatus(Status.ACTIVE);
                curriculums.add(c5);
            }

            curriculumRepository.saveAll(curriculums);
            // Fetch saved curriculums and map by programId for easy lookup
            for (Curriculum c : curriculumRepository.findAll()) {
                curriculumMap.put(c.getProgram().getId(), c);
            }
            System.out.println("[DEBUG] Saved curriculums: " + curriculums.size());

            // Now insert curriculum courses
            if (curriculumCourseRepository.count() == 0) {
                List<CurriculumCourse> curriculumCourses = new ArrayList<>();
                Object[][] entries = new Object[][]{
                    // Format: {required, semester, yearLevel, courseId, curriculumId}
                    {true, "1st Semester", 1, 1L, 1L},
                    {true, "1st Semester", 1, 2L, 1L},
                    {true, "1st Semester", 1, 3L, 1L},
                    {true, "1st Semester", 1, 6L, 1L},
                    {true, "1st Semester", 1, 7L, 1L},
                    {true, "2nd Semester", 1, 4L, 1L},
                    {true, "2nd Semester", 1, 5L, 1L},
                    {true, "2nd Semester", 1, 8L, 1L},
                    {true, "2nd Semester", 1, 9L, 1L},
                    {true, "1st Semester", 2, 10L, 1L},
                    {true, "1st Semester", 2, 11L, 1L},
                    {true, "1st Semester", 2, 12L, 1L},
                    {true, "2nd Semester", 2, 13L, 1L},
                    {true, "2nd Semester", 2, 14L, 1L},
                    {true, "2nd Semester", 2, 15L, 1L},
                    {true, "1st Semester", 3, 16L, 1L},
                    {true, "1st Semester", 3, 17L, 1L},
                    {true, "2nd Semester", 3, 18L, 1L},
                    {true, "2nd Semester", 3, 19L, 1L},
                    {true, "2nd Semester", 3, 20L, 1L},
                    {true, "1st Semester", 4, 21L, 1L},
                    {true, "2nd Semester", 4, 22L, 1L},
                    {true, "1st Semester", 1, 23L, 2L},
                    {true, "1st Semester", 1, 24L, 2L},
                    {true, "1st Semester", 1, 25L, 2L},
                    {true, "1st Semester", 1, 26L, 2L},
                    {true, "1st Semester", 1, 6L, 2L},
                    {true, "1st Semester", 1, 7L, 2L},
                    {true, "2nd Semester", 1, 27L, 2L},
                    {true, "2nd Semester", 1, 30L, 2L},
                    {true, "2nd Semester", 1, 28L, 2L},
                    {true, "2nd Semester", 1, 29L, 2L},
                    {true, "2nd Semester", 1, 8L, 2L},
                    {true, "2nd Semester", 1, 9L, 2L},
                    {true, "1st Semester", 2, 31L, 2L},
                    {true, "1st Semester", 2, 32L, 2L},
                    {true, "1st Semester", 2, 33L, 2L},
                    {true, "1st Semester", 2, 34L, 2L},
                    {true, "1st Semester", 2, 35L, 2L},
                    {true, "1st Semester", 2, 15L, 2L},
                    {true, "2nd Semester", 2, 36L, 2L},
                    {true, "2nd Semester", 2, 37L, 2L},
                    {true, "2nd Semester", 2, 38L, 2L},
                    {true, "2nd Semester", 2, 39L, 2L},
                    {true, "2nd Semester", 2, 40L, 2L},
                    {true, "1st Semester", 3, 41L, 2L},
                    {true, "1st Semester", 3, 42L, 2L},
                    {true, "1st Semester", 3, 43L, 2L},
                    {true, "1st Semester", 3, 44L, 2L},
                    {true, "2nd Semester", 3, 45L, 2L},
                    {true, "2nd Semester", 3, 46L, 2L},
                    {true, "2nd Semester", 3, 47L, 2L},
                    {true, "2nd Semester", 3, 48L, 2L},
                    {true, "1st Semester", 4, 49L, 2L},
                    {true, "1st Semester", 4, 50L, 2L},
                    {true, "2nd Semester", 4, 51L, 2L},
                    {true, "2nd Semester", 2, 20L, 2L},
                    {true, "1st Semester", 1, 84L, 3L},
                    {true, "1st Semester", 1, 85L, 3L},
                    {true, "1st Semester", 1, 86L, 3L},
                    {true, "1st Semester", 1, 6L, 3L},
                    {true, "1st Semester", 1, 7L, 3L},
                    {true, "2nd Semester", 1, 87L, 3L},
                    {true, "2nd Semester", 1, 88L, 3L},
                    {true, "2nd Semester", 1, 8L, 3L},
                    {true, "2nd Semester", 1, 9L, 3L},
                    {true, "1st Semester", 2, 89L, 3L},
                    {true, "1st Semester", 2, 90L, 3L},
                    {true, "1st Semester", 2, 91L, 3L},
                    {true, "2nd Semester", 2, 92L, 3L},
                    {true, "2nd Semester", 2, 93L, 3L},
                    {true, "2nd Semester", 2, 15L, 3L},
                    {true, "1st Semester", 3, 94L, 3L},
                    {true, "1st Semester", 3, 95L, 3L},
                    {true, "2nd Semester", 3, 96L, 3L},
                    {true, "2nd Semester", 3, 97L, 3L},
                    {true, "2nd Semester", 3, 20L, 3L},
                    {true, "1st Semester", 4, 98L, 3L},
                    {true, "2nd Semester", 4, 99L, 3L},
                    {true, "1st Semester", 1, 68L, 4L},
                    {true, "1st Semester", 1, 69L, 4L},
                    {true, "1st Semester", 1, 70L, 4L},
                    {true, "1st Semester", 1, 6L, 4L},
                    {true, "1st Semester", 1, 7L, 4L},
                    {true, "2nd Semester", 1, 71L, 4L},
                    {true, "2nd Semester", 1, 72L, 4L},
                    {true, "2nd Semester", 1, 8L, 4L},
                    {true, "2nd Semester", 1, 9L, 4L},
                    {true, "1st Semester", 2, 73L, 4L},
                    {true, "1st Semester", 2, 74L, 4L},
                    {true, "1st Semester", 2, 75L, 4L},
                    {true, "2nd Semester", 2, 76L, 4L},
                    {true, "2nd Semester", 2, 77L, 4L},
                    {true, "2nd Semester", 2, 15L, 4L},
                    {true, "1st Semester", 3, 78L, 4L},
                    {true, "1st Semester", 3, 79L, 4L},
                    {true, "2nd Semester", 3, 80L, 4L},
                    {true, "2nd Semester", 3, 81L, 4L},
                    {true, "2nd Semester", 3, 20L, 4L},
                    {true, "1st Semester", 4, 82L, 4L},
                    {true, "2nd Semester", 4, 83L, 4L},
                    {true, "1st Semester", 1, 52L, 5L},
                    {true, "1st Semester", 1, 53L, 5L},
                    {true, "1st Semester", 1, 54L, 5L},
                    {true, "1st Semester", 1, 6L, 5L},
                    {true, "1st Semester", 1, 7L, 5L},
                    {true, "2nd Semester", 1, 55L, 5L},
                    {true, "2nd Semester", 1, 56L, 5L},
                    {true, "2nd Semester", 1, 8L, 5L},
                    {true, "2nd Semester", 1, 9L, 5L},
                    {true, "1st Semester", 2, 57L, 5L},
                    {true, "1st Semester", 2, 58L, 5L},
                    {true, "1st Semester", 2, 59L, 5L},
                    {true, "2nd Semester", 2, 60L, 5L},
                    {true, "2nd Semester", 2, 61L, 5L},
                    {true, "2nd Semester", 2, 15L, 5L},
                    {true, "1st Semester", 3, 62L, 5L},
                    {true, "1st Semester", 3, 63L, 5L},
                    {true, "2nd Semester", 3, 64L, 5L},
                    {true, "2nd Semester", 3, 65L, 5L},
                    {true, "2nd Semester", 3, 20L, 5L},
                    {true, "1st Semester", 4, 66L, 5L},
                    {true, "2nd Semester", 4, 67L, 5L},
                };
                for (Object[] entry : entries) {
                    boolean required = (boolean) entry[0];
                    String semester = (String) entry[1];
                    int yearLevel = (int) entry[2];
                    Long courseId = (Long) entry[3];
                    Long programCurriculumId = (Long) entry[4];
                    Curriculum curriculum = curriculumMap.get(programCurriculumId);
                    Course course = courseRepository.findById(courseId).orElse(null);
                    if (curriculum == null) {
                        System.out.println("[DEBUG] Curriculum not found: " + programCurriculumId);
                    }
                    if (course == null) {
                        System.out.println("[DEBUG] Course not found: " + courseId);
                    }
                    if (curriculum != null && course != null) {
                        CurriculumCourse cc = new CurriculumCourse();
                        cc.setCurriculum(curriculum);
                        cc.setCourse(course);
                        cc.setYearLevel(yearLevel);
                        cc.setSemester(semester);
                        cc.setRequired(required);
                        curriculumCourses.add(cc);
                    }
                }
                curriculumCourseRepository.saveAll(curriculumCourses);
                System.out.println("[DEBUG] Saved curriculum courses: " + curriculumCourses.size());
            }
        }
    }
}
