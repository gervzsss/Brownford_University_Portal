package com.brownford.data;

import com.brownford.model.*;
import com.brownford.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class AllDataInitializer implements CommandLineRunner {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CurriculumRepository curriculumRepository;
    @Autowired
    private CurriculumCourseRepository curriculumCourseRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public void run(String... args) {
        // 1. Insert Programs
        if (programRepository.count() == 0) {
            List<Program> programs = new ArrayList<>();
            Program p1 = new Program();
            p1.setCode("BSSTAT"); p1.setName("Bachelor of Science in Statistics"); p1.setStatus("Active"); p1.setTotalUnits(62); p1.setYears(4); programs.add(p1);
            Program p2 = new Program();
            p2.setCode("BSA"); p2.setName("Bachelor of Science in Accountancy"); p2.setStatus("Active"); p2.setTotalUnits(101); p2.setYears(4); programs.add(p2);
            Program p3 = new Program();
            p3.setCode("BSIT"); p3.setName("Bachelor of Science in Information Technology"); p3.setStatus("Active"); p3.setTotalUnits(56); p3.setYears(4); programs.add(p3);
            Program p4 = new Program();
            p4.setCode("BSPSYCH"); p4.setName("Bachelor of Science in Psychology"); p4.setStatus("Active"); p4.setTotalUnits(51); p4.setYears(4); programs.add(p4);
            Program p5 = new Program();
            p5.setCode("BSMM"); p5.setName("Bachelor of Science in Marketing Management"); p5.setStatus("Active"); p5.setTotalUnits(62); p5.setYears(4); programs.add(p5);
            programRepository.saveAll(programs);
            System.out.println("[INFO] Default programs inserted.");
        }

        // 2. Insert Courses
        if (courseRepository.count() == 0) {
            List<Course> courses = new ArrayList<>();
            // --- Course 1 ---
            Course c1 = new Course();
            c1.setCourseCode("STAT101");
            c1.setCourseTitle("Fundamentals of Statistics");
            c1.setPrerequisites("None");
            c1.setCorequisites("None");
            c1.setSemester("1st Semester");
            c1.setStatus("active");
            c1.setUnits(3);
            c1.setYearLevel(1);
            courses.add(c1);
            // --- Course 2 ---
            Course c2 = new Course();
            c2.setCourseCode("STAT102");
            c2.setCourseTitle("Probability Theory");
            c2.setPrerequisites("STAT101");
            c2.setCorequisites("None");
            c2.setSemester("1st Semester");
            c2.setStatus("active");
            c2.setUnits(3);
            c2.setYearLevel(1);
            courses.add(c2);
            // --- Course 3 ---
            Course c3 = new Course();
            c3.setCourseCode("STAT103");
            c3.setCourseTitle("Statistical Computing");
            c3.setPrerequisites("STAT101");
            c3.setCorequisites("None");
            c3.setSemester("1st Semester");
            c3.setStatus("active");
            c3.setUnits(3);
            c3.setYearLevel(1);
            courses.add(c3);
            // --- Course 4 ---
            Course c4 = new Course();
            c4.setCourseCode("STAT104");
            c4.setCourseTitle("Linear Regression");
            c4.setPrerequisites("STAT102, STAT103");
            c4.setCorequisites("None");
            c4.setSemester("2nd Semester");
            c4.setStatus("active");
            c4.setUnits(3);
            c4.setYearLevel(1);
            courses.add(c4);
            // --- Course 5 ---
            Course c5 = new Course();
            c5.setCourseCode("STAT105");
            c5.setCourseTitle("Data Mining");
            c5.setPrerequisites("STAT104");
            c5.setCorequisites("None");
            c5.setSemester("2nd Semester");
            c5.setStatus("active");
            c5.setUnits(3);
            c5.setYearLevel(1);
            courses.add(c5);
            // --- Course 6 ---
            Course c6 = new Course();
            c6.setCourseCode("NSTP101");
            c6.setCourseTitle("National Service Training Program 1");
            c6.setPrerequisites("None");
            c6.setCorequisites("None");
            c6.setSemester("1st Semester");
            c6.setStatus("active");
            c6.setUnits(3);
            c6.setYearLevel(1);
            courses.add(c6);
            // --- Course 7 ---
            Course c7 = new Course();
            c7.setCourseCode("PATHFIT1");
            c7.setCourseTitle("Physical Activity Towards Health and Fitness 1");
            c7.setPrerequisites("None");
            c7.setCorequisites("None");
            c7.setSemester("1st Semester");
            c7.setStatus("active");
            c7.setUnits(2);
            c7.setYearLevel(1);
            courses.add(c7);
            // --- Course 8 ---
            Course c8 = new Course();
            c8.setCourseCode("NSTP102");
            c8.setCourseTitle("National Service Training Program 2");
            c8.setPrerequisites("NSTP101");
            c8.setCorequisites("None");
            c8.setSemester("2nd Semester");
            c8.setStatus("active");
            c8.setUnits(2);
            c8.setYearLevel(1);
            courses.add(c8);
            // --- Course 9 ---
            Course c9 = new Course();
            c9.setCourseCode("PATHFIT2");
            c9.setCourseTitle("Physical Activity Towards Health and Fitness 2");
            c9.setPrerequisites("PATHFIT1");
            c9.setCorequisites("None");
            c9.setSemester("2nd Semester");
            c9.setStatus("active");
            c9.setUnits(2);
            c9.setYearLevel(1);
            courses.add(c9);
            // --- Course 10 ---
            Course c10 = new Course();
            c10.setCourseCode("STAT201");
            c10.setCourseTitle("Multivariate Analysis");
            c10.setPrerequisites("STAT104");
            c10.setCorequisites("None");
            c10.setSemester("1st Semester");
            c10.setStatus("active");
            c10.setUnits(3);
            c10.setYearLevel(2);
            courses.add(c10);
            // --- Course 11 ---
            Course c11 = new Course();
            c11.setCourseCode("STAT202");
            c11.setCourseTitle("Time Series Analysis");
            c11.setPrerequisites("STAT104");
            c11.setCorequisites("None");
            c11.setSemester("1st Semester");
            c11.setStatus("active");
            c11.setUnits(3);
            c11.setYearLevel(2);
            courses.add(c11);
            // --- Course 12 ---
            Course c12 = new Course();
            c12.setCourseCode("STAT203");
            c12.setCourseTitle("Experimental Design");
            c12.setPrerequisites("STAT201");
            c12.setCorequisites("None");
            c12.setSemester("1st Semester");
            c12.setStatus("active");
            c12.setUnits(3);
            c12.setYearLevel(2);
            courses.add(c12);
            // --- Course 13 ---
            Course c13 = new Course();
            c13.setCourseCode("STAT204");
            c13.setCourseTitle("Statistical Inference");
            c13.setPrerequisites("STAT202");
            c13.setCorequisites("None");
            c13.setSemester("2nd Semester");
            c13.setStatus("active");
            c13.setUnits(3);
            c13.setYearLevel(2);
            courses.add(c13);
            // --- Course 14 ---
            Course c14 = new Course();
            c14.setCourseCode("STAT205");
            c14.setCourseTitle("Categorical Data Analysis");
            c14.setPrerequisites("STAT201");
            c14.setCorequisites("None");
            c14.setSemester("2nd Semester");
            c14.setStatus("active");
            c14.setUnits(3);
            c14.setYearLevel(2);
            courses.add(c14);
            // --- Course 15 ---
            Course c15 = new Course();
            c15.setCourseCode("PATHFIT3");
            c15.setCourseTitle("Physical Activity Towards Health and Fitness 3");
            c15.setPrerequisites("PATHFIT2");
            c15.setCorequisites("None");
            c15.setSemester("2nd Semester");
            c15.setStatus("active");
            c15.setUnits(2);
            c15.setYearLevel(2);
            courses.add(c15);
            // --- Course 16 ---
            Course c16 = new Course();
            c16.setCourseCode("STAT301");
            c16.setCourseTitle("Advanced Statistical Modeling");
            c16.setPrerequisites("STAT204");
            c16.setCorequisites("None");
            c16.setSemester("1st Semester");
            c16.setStatus("active");
            c16.setUnits(3);
            c16.setYearLevel(3);
            courses.add(c16);
            // --- Course 17 ---
            Course c17 = new Course();
            c17.setCourseCode("STAT302");
            c17.setCourseTitle("Applied Stochastic Processes");
            c17.setPrerequisites("STAT204");
            c17.setCorequisites("None");
            c17.setSemester("1st Semester");
            c17.setStatus("active");
            c17.setUnits(3);
            c17.setYearLevel(3);
            courses.add(c17);
            // --- Course 18 ---
            Course c18 = new Course();
            c18.setCourseCode("STAT303");
            c18.setCourseTitle("Statistical Consulting");
            c18.setPrerequisites("STAT301");
            c18.setCorequisites("None");
            c18.setSemester("2nd Semester");
            c18.setStatus("active");
            c18.setUnits(3);
            c18.setYearLevel(3);
            courses.add(c18);
            // --- Course 19 ---
            Course c19 = new Course();
            c19.setCourseCode("STAT304");
            c19.setCourseTitle("Nonparametric Statistics");
            c19.setPrerequisites("STAT301");
            c19.setCorequisites("None");
            c19.setSemester("2nd Semester");
            c19.setStatus("active");
            c19.setUnits(3);
            c19.setYearLevel(3);
            courses.add(c19);
            // --- Course 20 ---
            Course c20 = new Course();
            c20.setCourseCode("PATHFIT4");
            c20.setCourseTitle("Physical Activity Towards Health and Fitness 4");
            c20.setPrerequisites("PATHFIT3");
            c20.setCorequisites("None");
            c20.setSemester("2nd Semester");
            c20.setStatus("active");
            c20.setUnits(2);
            c20.setYearLevel(3);
            courses.add(c20);
            // --- Course 21 ---
            Course c21 = new Course();
            c21.setCourseCode("STAT401");
            c21.setCourseTitle("Capstone Project 1");
            c21.setPrerequisites("STAT303");
            c21.setCorequisites("None");
            c21.setSemester("1st Semester");
            c21.setStatus("active");
            c21.setUnits(3);
            c21.setYearLevel(4);
            courses.add(c21);
            // --- Course 22 ---
            Course c22 = new Course();
            c22.setCourseCode("STAT402");
            c22.setCourseTitle("Capstone Project 2");
            c22.setPrerequisites("STAT401");
            c22.setCorequisites("None");
            c22.setSemester("2nd Semester");
            c22.setStatus("active");
            c22.setUnits(3);
            c22.setYearLevel(4);
            courses.add(c22);
            // --- Course 23 ---
            Course c23 = new Course();
            c23.setCourseCode("ACCTG101");
            c23.setCourseTitle("Financial Accounting and Reporting");
            c23.setPrerequisites("None");
            c23.setCorequisites("None");
            c23.setSemester("1st Semester");
            c23.setStatus("active");
            c23.setUnits(3);
            c23.setYearLevel(1);
            courses.add(c23);
            // --- Course 24 ---
            Course c24 = new Course();
            c24.setCourseCode("ACCTG102");
            c24.setCourseTitle("Conceptual Framework and Accounting Standards");
            c24.setPrerequisites("None");
            c24.setCorequisites("None");
            c24.setSemester("1st Semester");
            c24.setStatus("active");
            c24.setUnits(3);
            c24.setYearLevel(1);
            courses.add(c24);
            // --- Course 25 ---
            Course c25 = new Course();
            c25.setCourseCode("BUSLAW101");
            c25.setCourseTitle("Law on Obligations and Contracts");
            c25.setPrerequisites("None");
            c25.setCorequisites("None");
            c25.setSemester("1st Semester");
            c25.setStatus("active");
            c25.setUnits(3);
            c25.setYearLevel(1);
            courses.add(c25);
            // --- Course 26 ---
            Course c26 = new Course();
            c26.setCourseCode("ECCOM101");
            c26.setCourseTitle("Fundamentals of Economics");
            c26.setPrerequisites("None");
            c26.setCorequisites("None");
            c26.setSemester("1st Semester");
            c26.setStatus("active");
            c26.setUnits(3);
            c26.setYearLevel(1);
            courses.add(c26);
            // --- Course 27 ---
            Course c27 = new Course();
            c27.setCourseCode("ACCTG103");
            c27.setCourseTitle("Intermediate Accounting 1");
            c27.setPrerequisites("ACCTG101");
            c27.setCorequisites("None");
            c27.setSemester("2nd Semester");
            c27.setStatus("active");
            c27.setUnits(3);
            c27.setYearLevel(1);
            courses.add(c27);
            // --- Course 28 ---
            Course c28 = new Course();
            c28.setCourseCode("BUSLAW102");
            c28.setCourseTitle("Law on Business Organizations");
            c28.setPrerequisites("BUSLAW101");
            c28.setCorequisites("None");
            c28.setSemester("2nd Semester");
            c28.setStatus("active");
            c28.setUnits(3);
            c28.setYearLevel(1);
            courses.add(c28);
            // --- Course 29 ---
            Course c29 = new Course();
            c29.setCourseCode("QUANSCI101");
            c29.setCourseTitle("Business Statistics");
            c29.setPrerequisites("None");
            c29.setCorequisites("None");
            c29.setSemester("2nd Semester");
            c29.setStatus("active");
            c29.setUnits(3);
            c29.setYearLevel(1);
            courses.add(c29);
            // --- Course 30 ---
            Course c30 = new Course();
            c30.setCourseCode("ACCTG104");
            c30.setCourseTitle("Cost Accounting and Control");
            c30.setPrerequisites("ACCTG101");
            c30.setCorequisites("None");
            c30.setSemester("2nd Semester");
            c30.setStatus("active");
            c30.setUnits(3);
            c30.setYearLevel(1);
            courses.add(c30);
            // --- Course 31 ---
            Course c31 = new Course();
            c31.setCourseCode("ACCTG201");
            c31.setCourseTitle("Intermediate Accounting 2");
            c31.setPrerequisites("ACCTG103");
            c31.setCorequisites("None");
            c31.setSemester("1st Semester");
            c31.setStatus("active");
            c31.setUnits(3);
            c31.setYearLevel(2);
            courses.add(c31);
            // --- Course 32 ---
            Course c32 = new Course();
            c32.setCourseCode("ACCTG202");
            c32.setCourseTitle("Management Accounting");
            c32.setPrerequisites("ACCTG104");
            c32.setCorequisites("None");
            c32.setSemester("1st Semester");
            c32.setStatus("active");
            c32.setUnits(3);
            c32.setYearLevel(2);
            courses.add(c32);
            // --- Course 33 ---
            Course c33 = new Course();
            c33.setCourseCode("TAXN201");
            c33.setCourseTitle("Income Taxation");
            c33.setPrerequisites("ACCTG103");
            c33.setCorequisites("None");
            c33.setSemester("1st Semester");
            c33.setStatus("active");
            c33.setUnits(3);
            c33.setYearLevel(2);
            courses.add(c33);
            // --- Course 34 ---
            Course c34 = new Course();
            c34.setCourseCode("FINMAN201");
            c34.setCourseTitle("Financial Management 1");
            c34.setPrerequisites("ACCTG103, ECCOM101");
            c34.setCorequisites("None");
            c34.setSemester("1st Semester");
            c34.setStatus("active");
            c34.setUnits(3);
            c34.setYearLevel(2);
            courses.add(c34);
            // --- Course 35 ---
            Course c35 = new Course();
            c35.setCourseCode("AUDIT201");
            c35.setCourseTitle("Auditing and Assurance: Concepts and Applications");
            c35.setPrerequisites("ACCTG201");
            c35.setCorequisites("None");
            c35.setSemester("1st Semester");
            c35.setStatus("active");
            c35.setUnits(3);
            c35.setYearLevel(2);
            courses.add(c35);
            // --- Course 36 ---
            Course c36 = new Course();
            c36.setCourseCode("ACCTG203");
            c36.setCourseTitle("Intermediate Accounting 3");
            c36.setPrerequisites("ACCTG201");
            c36.setCorequisites("None");
            c36.setSemester("2nd Semester");
            c36.setStatus("active");
            c36.setUnits(3);
            c36.setYearLevel(2);
            courses.add(c36);
            // --- Course 37 ---
            Course c37 = new Course();
            c37.setCourseCode("ACCTG204");
            c37.setCourseTitle("Government Accounting");
            c37.setPrerequisites("ACCTG201");
            c37.setCorequisites("None");
            c37.setSemester("2nd Semester");
            c37.setStatus("active");
            c37.setUnits(3);
            c37.setYearLevel(2);
            courses.add(c37);
            // --- Course 38 ---
            Course c38 = new Course();
            c38.setCourseCode("TAXN202");
            c38.setCourseTitle("Business and Transfer Taxation");
            c38.setPrerequisites("TAXN201");
            c38.setCorequisites("None");
            c38.setSemester("2nd Semester");
            c38.setStatus("active");
            c38.setUnits(3);
            c38.setYearLevel(2);
            courses.add(c38);
            // --- Course 39 ---
            Course c39 = new Course();
            c39.setCourseCode("FINMAN202");
            c39.setCourseTitle("Financial Markets and Institutions");
            c39.setPrerequisites("FINMAN201");
            c39.setCorequisites("None");
            c39.setSemester("2nd Semester");
            c39.setStatus("active");
            c39.setUnits(3);
            c39.setYearLevel(2);
            courses.add(c39);
            // --- Course 40 ---
            Course c40 = new Course();
            c40.setCourseCode("AUDIT202");
            c40.setCourseTitle("Auditing and Assurance: Specialized Applications");
            c40.setPrerequisites("AUDIT201");
            c40.setCorequisites("None");
            c40.setSemester("2nd Semester");
            c40.setStatus("active");
            c40.setUnits(3);
            c40.setYearLevel(2);
            courses.add(c40);
            // --- Course 41 ---
            Course c41 = new Course();
            c41.setCourseCode("ACCTG301");
            c41.setCourseTitle("Advanced Financial Accounting and Reporting 1");
            c41.setPrerequisites("ACCTG203");
            c41.setCorequisites("None");
            c41.setSemester("1st Semester");
            c41.setStatus("active");
            c41.setUnits(3);
            c41.setYearLevel(3);
            courses.add(c41);
            // --- Course 42 ---
            Course c42 = new Course();
            c42.setCourseCode("ACCTG302");
            c42.setCourseTitle("Accounting Information System");
            c42.setPrerequisites("ACCTG203");
            c42.setCorequisites("None");
            c42.setSemester("1st Semester");
            c42.setStatus("active");
            c42.setUnits(3);
            c42.setYearLevel(3);
            courses.add(c42);
            // --- Course 43 ---
            Course c43 = new Course();
            c43.setCourseCode("AUDIT301");
            c43.setCourseTitle("Auditing and Assurance: Principles and Standards");
            c43.setPrerequisites("AUDIT202");
            c43.setCorequisites("None");
            c43.setSemester("1st Semester");
            c43.setStatus("active");
            c43.setUnits(3);
            c43.setYearLevel(3);
            courses.add(c43);
            // --- Course 44 ---
            Course c44 = new Course();
            c44.setCourseCode("PROFETH301");
            c44.setCourseTitle("Good Governance and Social Responsibility");
            c44.setPrerequisites("None");
            c44.setCorequisites("None");
            c44.setSemester("1st Semester");
            c44.setStatus("active");
            c44.setUnits(3);
            c44.setYearLevel(3);
            courses.add(c44);
            // --- Course 45 ---
            Course c45 = new Course();
            c45.setCourseCode("ACCTG303");
            c45.setCourseTitle("Advanced Financial Accounting and Reporting 2");
            c45.setPrerequisites("ACCTG301");
            c45.setCorequisites("None");
            c45.setSemester("2nd Semester");
            c45.setStatus("active");
            c45.setUnits(3);
            c45.setYearLevel(3);
            courses.add(c45);
            // --- Course 46 ---
            Course c46 = new Course();
            c46.setCourseCode("ACCTG304");
            c46.setCourseTitle("Strategic Cost Management");
            c46.setPrerequisites("ACCTG202");
            c46.setCorequisites("None");
            c46.setSemester("2nd Semester");
            c46.setStatus("active");
            c46.setUnits(3);
            c46.setYearLevel(3);
            courses.add(c46);
            // --- Course 47 ---
            Course c47 = new Course();
            c47.setCourseCode("AUDIT302");
            c47.setCourseTitle("Auditing and Assurance: Engagement Practice");
            c47.setPrerequisites("AUDIT301");
            c47.setCorequisites("None");
            c47.setSemester("2nd Semester");
            c47.setStatus("active");
            c47.setUnits(3);
            c47.setYearLevel(3);
            courses.add(c47);
            // --- Course 48 ---
            Course c48 = new Course();
            c48.setCourseCode("BUSPOL301");
            c48.setCourseTitle("Business Policy and Strategy");
            c48.setPrerequisites("FINMAN202, PROFETH301");
            c48.setCorequisites("None");
            c48.setSemester("2nd Semester");
            c48.setStatus("active");
            c48.setUnits(3);
            c48.setYearLevel(3);
            courses.add(c48);
            // --- Course 49 ---
            Course c49 = new Course();
            c49.setCourseCode("ACCTG401");
            c49.setCourseTitle("Capstone Project 1 (Accounting Research)");
            c49.setPrerequisites("ACCTG303, AUDIT302");
            c49.setCorequisites("None");
            c49.setSemester("1st Semester");
            c49.setStatus("active");
            c49.setUnits(3);
            c49.setYearLevel(4);
            courses.add(c49);
            // --- Course 50 ---
            Course c50 = new Course();
            c50.setCourseCode("TAXN401");
            c50.setCourseTitle("Advanced Taxation");
            c50.setPrerequisites("TAXN202");
            c50.setCorequisites("None");
            c50.setSemester("1st Semester");
            c50.setStatus("active");
            c50.setUnits(3);
            c50.setYearLevel(4);
            courses.add(c50);
            // --- Course 51 ---
            Course c51 = new Course();
            c51.setCourseCode("ACCTG402");
            c51.setCourseTitle("Capstone Project 2 (Comprehensive Examination)");
            c51.setPrerequisites("ACCTG401");
            c51.setCorequisites("None");
            c51.setSemester("2nd Semester");
            c51.setStatus("active");
            c51.setUnits(3);
            c51.setYearLevel(4);
            courses.add(c51);
            // --- Course 52 ---
            Course c52 = new Course();
            c52.setCourseCode("MM101");
            c52.setCourseTitle("Principles of Marketing");
            c52.setPrerequisites("None");
            c52.setCorequisites("None");
            c52.setSemester("1st Semester");
            c52.setStatus("active");
            c52.setUnits(3);
            c52.setYearLevel(1);
            courses.add(c52);
            // --- Course 53 ---
            Course c53 = new Course();
            c53.setCourseCode("MM102");
            c53.setCourseTitle("Consumer Behavior");
            c53.setPrerequisites("MM101");
            c53.setCorequisites("None");
            c53.setSemester("1st Semester");
            c53.setStatus("active");
            c53.setUnits(3);
            c53.setYearLevel(1);
            courses.add(c53);
            // --- Course 54 ---
            Course c54 = new Course();
            c54.setCourseCode("MM103");
            c54.setCourseTitle("Marketing Research");
            c54.setPrerequisites("MM101");
            c54.setCorequisites("None");
            c54.setSemester("1st Semester");
            c54.setStatus("active");
            c54.setUnits(3);
            c54.setYearLevel(1);
            courses.add(c54);
            // --- Course 55 ---
            Course c55 = new Course();
            c55.setCourseCode("MM104");
            c55.setCourseTitle("Digital Marketing");
            c55.setPrerequisites("MM102");
            c55.setCorequisites("None");
            c55.setSemester("2nd Semester");
            c55.setStatus("active");
            c55.setUnits(3);
            c55.setYearLevel(1);
            courses.add(c55);
            // --- Course 56 ---
            Course c56 = new Course();
            c56.setCourseCode("MM105");
            c56.setCourseTitle("Brand Management");
            c56.setPrerequisites("MM103");
            c56.setCorequisites("None");
            c56.setSemester("2nd Semester");
            c56.setStatus("active");
            c56.setUnits(3);
            c56.setYearLevel(1);
            courses.add(c56);
            // --- Course 57 ---
            Course c57 = new Course();
            c57.setCourseCode("MM201");
            c57.setCourseTitle("Strategic Marketing");
            c57.setPrerequisites("MM104");
            c57.setCorequisites("None");
            c57.setSemester("1st Semester");
            c57.setStatus("active");
            c57.setUnits(3);
            c57.setYearLevel(2);
            courses.add(c57);
            // --- Course 58 ---
            Course c58 = new Course();
            c58.setCourseCode("MM202");
            c58.setCourseTitle("Sales Management");
            c58.setPrerequisites("MM104");
            c58.setCorequisites("None");
            c58.setSemester("1st Semester");
            c58.setStatus("active");
            c58.setUnits(3);
            c58.setYearLevel(2);
            courses.add(c58);
            // --- Course 59 ---
            Course c59 = new Course();
            c59.setCourseCode("MM203");
            c59.setCourseTitle("Marketing Analytics");
            c59.setPrerequisites("MM105");
            c59.setCorequisites("None");
            c59.setSemester("1st Semester");
            c59.setStatus("active");
            c59.setUnits(3);
            c59.setYearLevel(2);
            courses.add(c59);
            // --- Course 60 ---
            Course c60 = new Course();
            c60.setCourseCode("MM204");
            c60.setCourseTitle("International Marketing");
            c60.setPrerequisites("MM201");
            c60.setCorequisites("None");
            c60.setSemester("2nd Semester");
            c60.setStatus("active");
            c60.setUnits(3);
            c60.setYearLevel(2);
            courses.add(c60);
            // --- Course 61 ---
            Course c61 = new Course();
            c61.setCourseCode("MM205");
            c61.setCourseTitle("Retail Management");
            c61.setPrerequisites("MM201");
            c61.setCorequisites("None");
            c61.setSemester("2nd Semester");
            c61.setStatus("active");
            c61.setUnits(3);
            c61.setYearLevel(2);
            courses.add(c61);
            // --- Course 62 ---
            Course c62 = new Course();
            c62.setCourseCode("MM301");
            c62.setCourseTitle("Marketing Communication");
            c62.setPrerequisites("MM203");
            c62.setCorequisites("None");
            c62.setSemester("1st Semester");
            c62.setStatus("active");
            c62.setUnits(3);
            c62.setYearLevel(3);
            courses.add(c62);
            // --- Course 63 ---
            Course c63 = new Course();
            c63.setCourseCode("MM302");
            c63.setCourseTitle("E-commerce Marketing");
            c63.setPrerequisites("MM203");
            c63.setCorequisites("None");
            c63.setSemester("1st Semester");
            c63.setStatus("active");
            c63.setUnits(3);
            c63.setYearLevel(3);
            courses.add(c63);
            // --- Course 64 ---
            Course c64 = new Course();
            c64.setCourseCode("MM303");
            c64.setCourseTitle("Marketing Strategy and Planning");
            c64.setPrerequisites("MM301");
            c64.setCorequisites("None");
            c64.setSemester("2nd Semester");
            c64.setStatus("active");
            c64.setUnits(3);
            c64.setYearLevel(3);
            courses.add(c64);
            // --- Course 65 ---
            Course c65 = new Course();
            c65.setCourseCode("MM304");
            c65.setCourseTitle("Product Management");
            c65.setPrerequisites("MM301");
            c65.setCorequisites("None");
            c65.setSemester("2nd Semester");
            c65.setStatus("active");
            c65.setUnits(3);
            c65.setYearLevel(3);
            courses.add(c65);
            // --- Course 66 ---
            Course c66 = new Course();
            c66.setCourseCode("MM401");
            c66.setCourseTitle("Capstone Project 1");
            c66.setPrerequisites("MM303");
            c66.setCorequisites("None");
            c66.setSemester("1st Semester");
            c66.setStatus("active");
            c66.setUnits(3);
            c66.setYearLevel(4);
            courses.add(c66);
            // --- Course 67 ---
            Course c67 = new Course();
            c67.setCourseCode("MM402");
            c67.setCourseTitle("Capstone Project 2");
            c67.setPrerequisites("MM401");
            c67.setCorequisites("None");
            c67.setSemester("2nd Semester");
            c67.setStatus("active");
            c67.setUnits(3);
            c67.setYearLevel(4);
            courses.add(c67);
            // --- Course 68 ---
            Course c68 = new Course();
            c68.setCourseCode("PSYCH101");
            c68.setCourseTitle("Introduction to Psychology");
            c68.setPrerequisites("None");
            c68.setCorequisites("None");
            c68.setSemester("1st Semester");
            c68.setStatus("active");
            c68.setUnits(3);
            c68.setYearLevel(1);
            courses.add(c68);
            // --- Course 69 ---
            Course c69 = new Course();
            c69.setCourseCode("PSYCH102");
            c69.setCourseTitle("Biological Psychology");
            c69.setPrerequisites("PSYCH101");
            c69.setCorequisites("None");
            c69.setSemester("1st Semester");
            c69.setStatus("active");
            c69.setUnits(3);
            c69.setYearLevel(1);
            courses.add(c69);
            // --- Course 70 ---
            Course c70 = new Course();
            c70.setCourseCode("PSYCH103");
            c70.setCourseTitle("Developmental Psychology");
            c70.setPrerequisites("PSYCH101");
            c70.setCorequisites("None");
            c70.setSemester("1st Semester");
            c70.setStatus("active");
            c70.setUnits(3);
            c70.setYearLevel(1);
            courses.add(c70);
            // --- Course 71 ---
            Course c71 = new Course();
            c71.setCourseCode("PSYCH104");
            c71.setCourseTitle("Cognitive Psychology");
            c71.setPrerequisites("PSYCH102");
            c71.setCorequisites("None");
            c71.setSemester("2nd Semester");
            c71.setStatus("active");
            c71.setUnits(3);
            c71.setYearLevel(1);
            courses.add(c71);
            // --- Course 72 ---
            Course c72 = new Course();
            c72.setCourseCode("PSYCH105");
            c72.setCourseTitle("Social Psychology");
            c72.setPrerequisites("PSYCH102");
            c72.setCorequisites("None");
            c72.setSemester("2nd Semester");
            c72.setStatus("active");
            c72.setUnits(3);
            c72.setYearLevel(1);
            courses.add(c72);
            // --- Course 73 ---
            Course c73 = new Course();
            c73.setCourseCode("PSYCH201");
            c73.setCourseTitle("Personality Theories");
            c73.setPrerequisites("PSYCH104");
            c73.setCorequisites("None");
            c73.setSemester("1st Semester");
            c73.setStatus("active");
            c73.setUnits(3);
            c73.setYearLevel(2);
            courses.add(c73);
            // --- Course 74 ---
            Course c74 = new Course();
            c74.setCourseCode("PSYCH202");
            c74.setCourseTitle("Abnormal Psychology");
            c74.setPrerequisites("PSYCH104");
            c74.setCorequisites("None");
            c74.setSemester("1st Semester");
            c74.setStatus("active");
            c74.setUnits(3);
            c74.setYearLevel(2);
            courses.add(c74);
            // --- Course 75 ---
            Course c75 = new Course();
            c75.setCourseCode("PSYCH203");
            c75.setCourseTitle("Research Methods in Psychology");
            c75.setPrerequisites("PSYCH105");
            c75.setCorequisites("None");
            c75.setSemester("1st Semester");
            c75.setStatus("active");
            c75.setUnits(3);
            c75.setYearLevel(2);
            courses.add(c75);
            // --- Course 76 ---
            Course c76 = new Course();
            c76.setCourseCode("PSYCH204");
            c76.setCourseTitle("Psychological Assessment");
            c76.setPrerequisites("PSYCH201");
            c76.setCorequisites("None");
            c76.setSemester("2nd Semester");
            c76.setStatus("active");
            c76.setUnits(3);
            c76.setYearLevel(2);
            courses.add(c76);
            // --- Course 77 ---
            Course c77 = new Course();
            c77.setCourseCode("PSYCH205");
            c77.setCourseTitle("Statistics for Psychology");
            c77.setPrerequisites("PSYCH203");
            c77.setCorequisites("None");
            c77.setSemester("2nd Semester");
            c77.setStatus("active");
            c77.setUnits(3);
            c77.setYearLevel(2);
            courses.add(c77);
            // --- Course 78 ---
            Course c78 = new Course();
            c78.setCourseCode("PSYCH301");
            c78.setCourseTitle("Counseling Psychology");
            c78.setPrerequisites("PSYCH203");
            c78.setCorequisites("None");
            c78.setSemester("1st Semester");
            c78.setStatus("active");
            c78.setUnits(3);
            c78.setYearLevel(3);
            courses.add(c78);
            // --- Course 79 ---
            Course c79 = new Course();
            c79.setCourseCode("PSYCH302");
            c79.setCourseTitle("Industrial/Organizational Psychology");
            c79.setPrerequisites("PSYCH203");
            c79.setCorequisites("None");
            c79.setSemester("1st Semester");
            c79.setStatus("active");
            c79.setUnits(3);
            c79.setYearLevel(3);
            courses.add(c79);
            // --- Course 80 ---
            Course c80 = new Course();
            c80.setCourseCode("PSYCH303");
            c80.setCourseTitle("Health Psychology");
            c80.setPrerequisites("PSYCH301");
            c80.setCorequisites("None");
            c80.setSemester("2nd Semester");
            c80.setStatus("active");
            c80.setUnits(3);
            c80.setYearLevel(3);
            courses.add(c80);
            // --- Course 81 ---
            Course c81 = new Course();
            c81.setCourseCode("PSYCH304");
            c81.setCourseTitle("Educational Psychology");
            c81.setPrerequisites("PSYCH301");
            c81.setCorequisites("None");
            c81.setSemester("2nd Semester");
            c81.setStatus("active");
            c81.setUnits(3);
            c81.setYearLevel(3);
            courses.add(c81);
            // --- Course 82 ---
            Course c82 = new Course();
            c82.setCourseCode("PSYCH401");
            c82.setCourseTitle("Capstone Project 1");
            c82.setPrerequisites("PSYCH303");
            c82.setCorequisites("None");
            c82.setSemester("1st Semester");
            c82.setStatus("active");
            c82.setUnits(3);
            c82.setYearLevel(4);
            courses.add(c82);
            // --- Course 83 ---
            Course c83 = new Course();
            c83.setCourseCode("PSYCH402");
            c83.setCourseTitle("Capstone Project 2");
            c83.setPrerequisites("PSYCH401");
            c83.setCorequisites("None");
            c83.setSemester("2nd Semester");
            c83.setStatus("active");
            c83.setUnits(3);
            c83.setYearLevel(4);
            courses.add(c83);
            // --- Course 84 ---
            Course c84 = new Course();
            c84.setCourseCode("IT101");
            c84.setCourseTitle("Programming Fundamentals");
            c84.setPrerequisites("None");
            c84.setCorequisites("None");
            c84.setSemester("1st Semester");
            c84.setStatus("active");
            c84.setUnits(3);
            c84.setYearLevel(1);
            courses.add(c84);
            // --- Course 85 ---
            Course c85 = new Course();
            c85.setCourseCode("IT102");
            c85.setCourseTitle("Database Systems");
            c85.setPrerequisites("IT101");
            c85.setCorequisites("None");
            c85.setSemester("1st Semester");
            c85.setStatus("active");
            c85.setUnits(3);
            c85.setYearLevel(1);
            courses.add(c85);
            // --- Course 86 ---
            Course c86 = new Course();
            c86.setCourseCode("IT103");
            c86.setCourseTitle("Networking Essentials");
            c86.setPrerequisites("IT101");
            c86.setCorequisites("None");
            c86.setSemester("1st Semester");
            c86.setStatus("active");
            c86.setUnits(3);
            c86.setYearLevel(1);
            courses.add(c86);
            // --- Course 87 ---
            Course c87 = new Course();
            c87.setCourseCode("IT104");
            c87.setCourseTitle("Web Development");
            c87.setPrerequisites("IT102");
            c87.setCorequisites("None");
            c87.setSemester("2nd Semester");
            c87.setStatus("active");
            c87.setUnits(3);
            c87.setYearLevel(1);
            courses.add(c87);
            // --- Course 88 ---
            Course c88 = new Course();
            c88.setCourseCode("IT105");
            c88.setCourseTitle("System Analysis and Design");
            c88.setPrerequisites("IT103");
            c88.setCorequisites("None");
            c88.setSemester("2nd Semester");
            c88.setStatus("active");
            c88.setUnits(3);
            c88.setYearLevel(1);
            courses.add(c88);
            // --- Course 89 ---
            Course c89 = new Course();
            c89.setCourseCode("IT201");
            c89.setCourseTitle("Advanced Programming");
            c89.setPrerequisites("IT104");
            c89.setCorequisites("None");
            c89.setSemester("1st Semester");
            c89.setStatus("active");
            c89.setUnits(3);
            c89.setYearLevel(2);
            courses.add(c89);
            // --- Course 90 ---
            Course c90 = new Course();
            c90.setCourseCode("IT202");
            c90.setCourseTitle("Operating Systems");
            c90.setPrerequisites("IT103");
            c90.setCorequisites("None");
            c90.setSemester("1st Semester");
            c90.setStatus("active");
            c90.setUnits(3);
            c90.setYearLevel(2);
            courses.add(c90);
            // --- Course 91 ---
            Course c91 = new Course();
            c91.setCourseCode("IT203");
            c91.setCourseTitle("Software Engineering");
            c91.setPrerequisites("IT105");
            c91.setCorequisites("None");
            c91.setSemester("1st Semester");
            c91.setStatus("active");
            c91.setUnits(3);
            c91.setYearLevel(2);
            courses.add(c91);
            // --- Course 92 ---
            Course c92 = new Course();
            c92.setCourseCode("IT204");
            c92.setCourseTitle("Mobile Application Development");
            c92.setPrerequisites("IT201");
            c92.setCorequisites("None");
            c92.setSemester("2nd Semester");
            c92.setStatus("active");
            c92.setUnits(3);
            c92.setYearLevel(2);
            courses.add(c92);
            // --- Course 93 ---
            Course c93 = new Course();
            c93.setCourseCode("IT205");
            c93.setCourseTitle("Data Structures and Algorithms");
            c93.setPrerequisites("IT201");
            c93.setCorequisites("None");
            c93.setSemester("2nd Semester");
            c93.setStatus("active");
            c93.setUnits(3);
            c93.setYearLevel(2);
            courses.add(c93);
            // --- Course 94 ---
            Course c94 = new Course();
            c94.setCourseCode("IT301");
            c94.setCourseTitle("Cloud Computing");
            c94.setPrerequisites("IT203");
            c94.setCorequisites("None");
            c94.setSemester("1st Semester");
            c94.setStatus("active");
            c94.setUnits(3);
            c94.setYearLevel(3);
            courses.add(c94);
            // --- Course 95 ---
            Course c95 = new Course();
            c95.setCourseCode("IT302");
            c95.setCourseTitle("Cybersecurity");
            c95.setPrerequisites("IT203");
            c95.setCorequisites("None");
            c95.setSemester("1st Semester");
            c95.setStatus("active");
            c95.setUnits(3);
            c95.setYearLevel(3);
            courses.add(c95);
            // --- Course 96 ---
            Course c96 = new Course();
            c96.setCourseCode("IT303");
            c96.setCourseTitle("IT Project Management");
            c96.setPrerequisites("IT301");
            c96.setCorequisites("None");
            c96.setSemester("2nd Semester");
            c96.setStatus("active");
            c96.setUnits(3);
            c96.setYearLevel(3);
            courses.add(c96);
            // --- Course 97 ---
            Course c97 = new Course();
            c97.setCourseCode("IT304");
            c97.setCourseTitle("Artificial Intelligence Fundamentals");
            c97.setPrerequisites("IT301");
            c97.setCorequisites("None");
            c97.setSemester("2nd Semester");
            c97.setStatus("active");
            c97.setUnits(3);
            c97.setYearLevel(3);
            courses.add(c97);
            // --- Course 98 ---
            Course c98 = new Course();
            c98.setCourseCode("IT401");
            c98.setCourseTitle("Capstone Project 1");
            c98.setPrerequisites("IT303");
            c98.setCorequisites("None");
            c98.setSemester("1st Semester");
            c98.setStatus("active");
            c98.setUnits(3);
            c98.setYearLevel(4);
            courses.add(c98);
            // --- Course 99 ---
            Course c99 = new Course();
            c99.setCourseCode("IT402");
            c99.setCourseTitle("Capstone Project 2");
            c99.setPrerequisites("IT401");
            c99.setCorequisites("None");
            c99.setSemester("2nd Semester");
            c99.setStatus("active");
            c99.setUnits(3);
            c99.setYearLevel(4);
            courses.add(c99);
            courseRepository.saveAll(courses);
            System.out.println("[INFO] Default courses inserted.");
        }

        // 3. Insert Curriculums
        if (curriculumRepository.count() == 0) {
            List<Curriculum> curriculums = new ArrayList<>();
            Map<Long, Curriculum> curriculumMap = new HashMap<>();
            Program p1 = programRepository.findById(1L).orElse(null);
            Program p2 = programRepository.findById(2L).orElse(null);
            Program p3 = programRepository.findById(3L).orElse(null);
            Program p4 = programRepository.findById(4L).orElse(null);
            Program p5 = programRepository.findById(5L).orElse(null);
            if (p1 != null) { Curriculum c1 = new Curriculum(); c1.setYearEffective(2025); c1.setProgram(p1); c1.setDescription("BSTAT CURRICULUM 2025"); c1.setStatus(Curriculum.Status.ACTIVE); curriculums.add(c1); }
            if (p2 != null) { Curriculum c2 = new Curriculum(); c2.setYearEffective(2025); c2.setProgram(p2); c2.setDescription("BSA CURRICULUM 2025"); c2.setStatus(Curriculum.Status.ACTIVE); curriculums.add(c2); }
            if (p3 != null) { Curriculum c3 = new Curriculum(); c3.setYearEffective(2025); c3.setProgram(p3); c3.setDescription("BSIT CURRICULUM 2025"); c3.setStatus(Curriculum.Status.ACTIVE); curriculums.add(c3); }
            if (p4 != null) { Curriculum c4 = new Curriculum(); c4.setYearEffective(2025); c4.setProgram(p4); c4.setDescription("BSPSYCH CURRICULUM 2025"); c4.setStatus(Curriculum.Status.ACTIVE); curriculums.add(c4); }
            if (p5 != null) { Curriculum c5 = new Curriculum(); c5.setYearEffective(2025); c5.setProgram(p5); c5.setDescription("BSMM CURRICULUM 2025"); c5.setStatus(Curriculum.Status.ACTIVE); curriculums.add(c5); }
            curriculumRepository.saveAll(curriculums);
            for (Curriculum c : curriculumRepository.findAll()) { curriculumMap.put(c.getProgram().getId(), c); }
            System.out.println("[INFO] Default curriculums inserted.");

            // 4. Insert Curriculum Courses
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
                System.out.println("[INFO] Default curriculum courses inserted.");
            }
        }

        // 5. Insert Sections
        if (sectionRepository.count() == 0) {
            List<Section> sections = new ArrayList<>();
            // id, maxStudents, sectionCode, status, curriculum_id, program_id
            Section s1 = new Section();
            s1.setMaxStudents(20);
            s1.setSectionCode("1-BSSTAT-2526");
            s1.setStatus("active");
            s1.setCurriculum(curriculumRepository.findById(1L).orElse(null));
            s1.setProgram(programRepository.findById(1L).orElse(null));
            sections.add(s1);

            Section s2 = new Section();
            s2.setMaxStudents(20);
            s2.setSectionCode("1-BSA-2526");
            s2.setStatus("active");
            s2.setCurriculum(curriculumRepository.findById(2L).orElse(null));
            s2.setProgram(programRepository.findById(2L).orElse(null));
            sections.add(s2);

            Section s3 = new Section();
            s3.setMaxStudents(20);
            s3.setSectionCode("1-BSMM-2526");
            s3.setStatus("active");
            s3.setCurriculum(curriculumRepository.findById(5L).orElse(null));
            s3.setProgram(programRepository.findById(5L).orElse(null));
            sections.add(s3);

            Section s4 = new Section();
            s4.setMaxStudents(20);
            s4.setSectionCode("1-BSIT-2526");
            s4.setStatus("active");
            s4.setCurriculum(curriculumRepository.findById(3L).orElse(null));
            s4.setProgram(programRepository.findById(3L).orElse(null));
            sections.add(s4);

            Section s5 = new Section();
            s5.setMaxStudents(20);
            s5.setSectionCode("1-BSPSYCH-2526");
            s5.setStatus("active");
            s5.setCurriculum(curriculumRepository.findById(4L).orElse(null));
            s5.setProgram(programRepository.findById(4L).orElse(null));
            sections.add(s5);

            sectionRepository.saveAll(sections);
            System.out.println("[INFO] Default sections inserted.");
        }
    }
}
