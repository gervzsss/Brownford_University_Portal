package com.brownford.dto;

import java.util.List;

public class EnrollmentDTO {
    private Long id;
    private String studentId;
    private String studentName;
    private String yearLevel;
    private String semester;
    private String status;
    private Long sectionId;
    private String sectionName;
    private String programName;
    private List<CourseInfo> courses;
    // Add other fields as needed (e.g., course info)

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public List<CourseInfo> getCourses() { return courses; }
    public void setCourses(List<CourseInfo> courses) { this.courses = courses; }
}
