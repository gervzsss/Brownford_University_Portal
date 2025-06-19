package com.brownford.dto;

public class CourseInfo {
    private Long id;
    private String courseCode;
    private String courseTitle;

    public CourseInfo() {
    }

    public CourseInfo(Long id, String courseCode, String courseTitle) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
