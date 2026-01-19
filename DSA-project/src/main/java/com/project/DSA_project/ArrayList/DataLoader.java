package com.project.DSA_project.ArrayList;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataLoader {

    private ArrayList<Faculty> faculties = new ArrayList<>();

    public ArrayList<Faculty> loadData(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header
                Row row = sheet.getRow(i);
                if (row == null) continue; // skip empty row

                // Read and clean faculty, course, question
                String facultyName = getCellString(row.getCell(0));
                String courseName = getCellString(row.getCell(1));
                String questionText = getCellString(row.getCell(2));

                if (facultyName.isEmpty() || courseName.isEmpty() || questionText.isEmpty()) continue;

                // Read options
                String[] options = new String[4];
                for (int j = 0; j < 4; j++) {
                    options[j] = getCellString(row.getCell(3 + j));
                }

                // Read correct answer
                int correct = (int) row.getCell(7).getNumericCellValue();

                // Create Question
                Question question = new Question(questionText, options, correct);

                // Get or create Faculty and Course
                Faculty faculty = getOrCreateFaculty(facultyName);
                Course course = getOrCreateCourse(faculty, courseName);

                // Add question to course
                course.addQuestion(question);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return faculties;
    }

    // Helper to read cell as string safely
    private String getCellString(Cell cell) {
        if (cell == null) return "";
        String value = "";
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                value = String.valueOf((int) cell.getNumericCellValue());
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                value = "";
        }
        // Remove hidden/non-printable characters and trim spaces
        return value.replaceAll("[^\\p{Print}]", "").trim();
    }

    // Get existing Faculty or create new
    private Faculty getOrCreateFaculty(String name) {
        for (Faculty f : faculties) {
            if (f.getFacultyName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        Faculty newFaculty = new Faculty(name);
        faculties.add(newFaculty);
        return newFaculty;
    }

    // Get existing Course in faculty or create new
    private Course getOrCreateCourse(Faculty faculty, String courseName) {
        for (Course c : faculty.getCourses()) {
            if (c.getCourseName().equalsIgnoreCase(courseName)) {
                return c;
            }
        }
        Course newCourse = new Course(courseName);
        faculty.addCourse(newCourse);
        return newCourse;
    }
}