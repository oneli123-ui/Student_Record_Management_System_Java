// Author : Oneli Liyanage(10695938)
// Date : 10/2/2026
// Assignment : ASSIGNMENT 2 - CSP3341 Programming Languages and Paradigms
// File : FileHandler.java
// Functionality : Handles saving and loading student data to/from a CSV file (students.csv) in a pipe-delimited format.

package srms;

import java.io.*;
import java.util.*;

// FileHandler class - Manages saving and loading student data from CSV file
public class FileHandler {
    private static final String DATA_FILE = "students.csv";

    // Saves all students to students.csv in pipe-delimited format
    public static void saveStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Student student : students) {
                // Format: ID|Name|Subject1:Marks1,Subject2:Marks2,...
                StringBuilder line = new StringBuilder();
                line.append(student.getId()).append("|");
                line.append(student.getName()).append("|");

                boolean first = true;
                for (String subject : student.getSubjects()) {
                    if (!first) line.append(",");
                    line.append(subject).append(":").append(student.getSubjectMarks(subject));
                    first = false;
                }

                writer.println(line.toString());
            }
            System.out.println("✓ Data saved successfully.");
        } catch (IOException e) {
            System.out.println("✗ Error saving data: " + e.getMessage());
        }
    }

    // Loads all students from students.csv file
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(DATA_FILE);

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;

                String id = parts[0];
                String name = parts[1];
                Student student = new Student(id, name);

                // Parse subjects and marks from the CSV format
                if (parts.length > 2 && !parts[2].isEmpty()) {
                    String[] subjects = parts[2].split(",");
                    for (String subject : subjects) {
                        String[] subjectData = subject.split(":");
                        if (subjectData.length == 2) {
                            String subjectName = subjectData[0];
                            double marks = Double.parseDouble(subjectData[1]);
                            student.addSubject(subjectName, marks);
                        }
                    }
                }

                students.add(student);
            }
        } catch (IOException e) {
            System.out.println("✗ Error loading data: " + e.getMessage());
        }

        return students;
    }
}

