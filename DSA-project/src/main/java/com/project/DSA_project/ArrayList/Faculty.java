package com.project.DSA_project.ArrayList;

import java.util.ArrayList;

public class Faculty {

    private String facultyName;
    private ArrayList<Course> courses;

    public Faculty(String facultyName) {
        this.facultyName = facultyName;
        this.courses = new ArrayList<>();
    }

    public String getFacultyName() {
        return facultyName;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course c) {
        courses.add(c);
    }
}
