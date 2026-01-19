package com.project.DSA_project.ArrayList;

import java.util.ArrayList;

public class Course {

    private String courseName;
    private ArrayList<Question> questions;

    public Course(String courseName) {
        this.courseName = courseName;
        this.questions = new ArrayList<>();
    }

    public String getCourseName() {
        return courseName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }
}
