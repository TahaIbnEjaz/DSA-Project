package com.project.DSA_project.ArrayList;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        DataLoader loader = new DataLoader();
        ArrayList<Faculty> faculties = loader.loadData("C:\\Users\\AC\\OneDrive\\Desktop\\DSA-project\\src\\main\\resources\\static\\quiz.xlsx");

        // 1. Select Faculty
        System.out.println("Select Faculty:");
        for (int i = 0; i < faculties.size(); i++) {
            System.out.println((i + 1) + ". " + faculties.get(i).getFacultyName());
        }

        int facultyChoice = sc.nextInt() - 1;
        Faculty selectedFaculty = faculties.get(facultyChoice);

        // 2. Select Course
        System.out.println("\nSelect Course:");
        ArrayList<Course> courses = selectedFaculty.getCourses();

        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName());
        }

        int courseChoice = sc.nextInt() - 1;
        Course selectedCourse = courses.get(courseChoice);

        // 3. Start Quiz
        System.out.println("\n--- Quiz Started ---");
        ArrayList<Question> questions = selectedCourse.getQuestions();
        int score = 0;

        for (Question q : questions) {
            System.out.println("\nQ: " + q.getQuestionText());

            String[] options = q.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ") " + options[i]);
            }

            System.out.print("Your answer (1-4): ");
            int answer = sc.nextInt();

            if (answer == q.getCorrectAnswer()) {
                score++;
            }
        }

        int percentage = (score / 11) * 100;
        char grade;
        if (percentage >= 85 && percentage <= 100) {
            grade = 'A';
        } else if (percentage >= 70 && percentage <= 84) {
            grade = 'B';
        } else if (percentage >= 60 && percentage <= 69) {
            grade = 'C';
        } else if (percentage >= 50 && percentage <= 59) {
            grade = 'D';
        } else {
            grade = 'F';
        }

        System.out.println("\n--- Result ---");
        System.out.println("Total Questions: " + questions.size());
        System.out.println("Correct Answers: " + score);
        System.out.println("Score: " + (score * 100 / questions.size()) + "%");
        System.out.println("Grade: " + grade);

        sc.close();
    }
}
