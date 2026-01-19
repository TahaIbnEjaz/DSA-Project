package com.project.DSA_project;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

class LeaderboardEntry {
    private String name;
    private int percentage;
    private String course;

    public LeaderboardEntry(String name, int percentage, String course) {
        this.name = name;
        this.percentage = percentage;
        this.course = course;
    }

    // getters
    public String getName() { return name; }
    public int getPercentage() { return percentage; }
    public String getCourse() {
        return course;
    }
}


@Component
class LeaderboardExcel {

    static final String FILE_PATH = "C:\\Users\\AC\\OneDrive\\Desktop\\DSA-project\\src\\main\\resources\\static\\leaderboard.xlsx";

    // READ leaderboard
    public ArrayList<LeaderboardEntry> readLeaderboard() {

        ArrayList<LeaderboardEntry> list = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nameCell = row.getCell(0);
                Cell percentCell = row.getCell(1);
                Cell courseCell = row.getCell(2);

                if (nameCell == null || percentCell == null ) continue;

                String name = nameCell.getStringCellValue();
                int percentage = (int) percentCell.getNumericCellValue();
                String course = courseCell.getStringCellValue();

                list.add(new LeaderboardEntry(name, percentage, course));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // WRITE leaderboard
    public void saveResult(String name, int percentage, String course) {

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();

            Row row = sheet.createRow(rowCount + 1);
            row.createCell(0).setCellValue(name);
            row.createCell(1).setCellValue(percentage);
            row.createCell(2).setCellValue(course);

            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

