package com.brownford.dto;

public class FacultyAssignmentWithCourseDTO {
    // FacultyAssignment fields
    private Long assignmentId;
    private Long sectionId;
    private Long curriculumCourseId;
    private Long facultyId;
    private String facultyName;
    private String semester;
    private int yearLevel;
    private String day;
    private String startTime;
    private String endTime;
    private String room;

    // CurriculumCourse fields
    private String courseCode;
    private String courseTitle;
    private int courseYearLevel;
    private String courseSemester;

    // For frontend compatibility
    private String faculty;

    // Getters and setters
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
    public Long getCurriculumCourseId() { return curriculumCourseId; }
    public void setCurriculumCourseId(Long curriculumCourseId) { this.curriculumCourseId = curriculumCourseId; }
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public int getYearLevel() { return yearLevel; }
    public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public int getCourseYearLevel() { return courseYearLevel; }
    public void setCourseYearLevel(int courseYearLevel) { this.courseYearLevel = courseYearLevel; }
    public String getCourseSemester() { return courseSemester; }
    public void setCourseSemester(String courseSemester) { this.courseSemester = courseSemester; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
}
