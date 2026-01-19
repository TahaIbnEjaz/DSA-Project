package com.project.DSA_project;

import com.project.DSA_project.ArrayList.Course;
import com.project.DSA_project.ArrayList.DataLoader;
import com.project.DSA_project.ArrayList.Faculty;
import com.project.DSA_project.ArrayList.Question;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class QuizController {

    DataLoader dataLoader = new DataLoader();
    ArrayList<Faculty> faculties = dataLoader.loadData("C:\\Users\\AC\\OneDrive\\Desktop\\DSA-project\\src\\main\\resources\\static\\quiz.xlsx");

    @GetMapping("/")
    public String home() {
        return "index";
    }

    //Handling of user form page
    @GetMapping("/form")
    public String userPage() {
        return "form";
    }


    //Handling of main menu page
    @PostMapping("/Menu")
    public String handleFaculty(@RequestParam String faculty, @RequestParam String name, @RequestParam String age, @RequestParam String country, @RequestParam String email, HttpSession session) {
        System.out.println("received: " + faculty);
        System.out.println("received: " + name);
        System.out.println("received: " + age);
        System.out.println("received: " + country);
        System.out.println("received: " + email);

        if (faculty != null && !faculty.isEmpty()) {
            session.setAttribute("name", name);
            session.setAttribute("age", age);
            session.setAttribute("faculty", faculty);
            session.setAttribute("country", country);
            session.setAttribute("email", email);
        }

        if (faculty == null) {
            return "form";
        }

        return "redirect:/Menu";
    }

    @GetMapping("/Menu")
    public String mainPage(HttpSession session) {
        if (session.getAttribute("faculty") == null) {
            return "redirect:/"; // or formPage
        }

        return "Menu";
    }

    //Handling of quiz page
    @PostMapping("/Quiz")
    public String handleCourse(@RequestParam String course, HttpSession session, Model model) {

        session.setAttribute("course", course);

        String faculty = (String) session.getAttribute("faculty");

        Faculty selectedFaculty;
        if ("CS".equals(faculty)) {
            selectedFaculty = faculties.get(0);
        } else if ("BBA".equals(faculty)) {
            selectedFaculty = faculties.get(1);
        } else {
            selectedFaculty = faculties.get(2);
        }

        ArrayList<Course> courses = selectedFaculty.getCourses();

        Course selectedCourse;
        if ("OOP".equals(course) || "Management".equals(course) || "Anatomy".equals(course)) {
            selectedCourse = courses.get(0);
        } else if ("DSA".equals(course) || "Marketing".equals(course) || "Physiology".equals(course)) {
            selectedCourse = courses.get(1);
        } else {
            selectedCourse = courses.get(2);
        }

        ArrayList<Question> questions = selectedCourse.getQuestions();

        // Store quiz state in session
        session.setAttribute("questions", questions);
        session.setAttribute("currentQuestionIndex", 0);
        session.setAttribute("score", 0);
        int Qno = 1;
        session.setAttribute("Qno", Qno);

        Question currentQuestion = questions.get(0);
        model.addAttribute("question", currentQuestion);
        model.addAttribute("course", course);
        model.addAttribute("Qno", Qno);

        return "Quiz";
    }

    @GetMapping("/Quiz")
    public String quizPage(HttpSession session, Model model) {
        if (session.getAttribute("faculty") == null || session.getAttribute("course") == null) {
            return "redirect:/Menu";
        }

        return "Quiz";
    }

    @Autowired
    private LeaderboardExcel leaderboardExcel;

    @PostMapping("/Quiz/next")
    public String nextQuestion(@RequestParam int choosenOption, HttpSession session, Model model) {

        ArrayList<Question> questions = (ArrayList<Question>) session.getAttribute("questions");
        int index = (int) session.getAttribute("currentQuestionIndex");
        int score = (int) session.getAttribute("score");
        int Qno = (int) session.getAttribute("Qno");
        String name = (String) session.getAttribute("name");
        String course = (String) session.getAttribute("course");

        Question current = questions.get(index);

        //update score
        if (current.getCorrectAnswer() == choosenOption) {
            score++;
            session.setAttribute("score", score);
        }

        index++;
        session.setAttribute("currentQuestionIndex", index);
        Qno++;
        session.setAttribute("Qno", Qno);

        if (index >= questions.size()) {

            int totalQuestions = questions.size();
            int percentage = (int) ((score / (double) totalQuestions) * 100);
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


            leaderboardExcel.saveResult(name, percentage, course);

            model.addAttribute("score", score);
            model.addAttribute("grade", grade);
            model.addAttribute("percentage", percentage);

            return "result";
        }

        model.addAttribute("error", "Please select an option before clicking Next");
        model.addAttribute("question", questions.get(index));
        model.addAttribute("faculty", session.getAttribute("faculty"));
        model.addAttribute("course", session.getAttribute("course"));
        model.addAttribute("Qno", Qno);
        model.addAttribute("isLast", index == questions.size() - 1);



        return "Quiz";
    }
    @GetMapping("/Quiz/next")
    public String nextQuestion() {
        return "Quiz";
    }

//    @PostMapping("/result")
//    public String resultPage(HttpSession session, Model model) {
//
//        int correctAns = (int) session.getAttribute("score");
//
//        model.addAttribute("score", correctAns);
//
//        return "result";
//    }

    @GetMapping("/result")
    public String resultDisplay() {
        return "result";
    }

    @GetMapping("/thankyou")
    public String thankyouDisplay() {
        return "thankyou";
    }

    @GetMapping("/leaderboard")
    public String leaderboardPage(Model model) {

        // sort by highest percentage
        ArrayList<LeaderboardEntry> leaderboard = null;
        try {
            leaderboard = leaderboardExcel.readLeaderboard();
            leaderboard.sort((a, b) -> b.getPercentage() - a.getPercentage());
        } catch (Exception e) {
            e.printStackTrace(); // MUST print

        }
        model.addAttribute("leaderboard", leaderboard);
        return "leaderboard";
    }
}
