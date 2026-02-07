package srms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of student records using a CSV file.
 * Format per line: id,name,marks
 */
public class FileStorage {

    /**
     * Saves the list of students to a CSV file.
     *
     * @param students list of students to save
     * @param filename name of the CSV file
     */
    public static void save(List<Student> students, String filename) {
        if (students == null || filename == null || filename.isEmpty()) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student s : students) {
                // CSV format: id,name,marks
                writer.write(s.getId() + "," + s.getName() + "," + s.getMarks());
                writer.newLine();
            }
        } catch (IOException e) {
            // Fail silently to avoid crashing the application
            System.err.println("Error saving students to file: " + e.getMessage());
        }
    }

    /**
     * Loads students from a CSV file.
     *
     * @param filename name of the CSV file
     * @return list of loaded students (empty if file does not exist or error occurs)
     */
    public static List<Student> load(String filename) {
        List<Student> students = new ArrayList<>();

        if (filename == null || filename.isEmpty()) {
            return students;
        }

        File file = new File(filename);
        if (!file.exists()) {
            // File does not exist yet — return empty list
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Expecting: id,name,marks
                String[] parts = line.split(",");

                if (parts.length != 3) {
                    continue; // skip malformed lines
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                double marks;

                try {
                    marks = Double.parseDouble(parts[2].trim());
                } catch (NumberFormatException e) {
                    continue; // skip invalid marks
                }

                students.add(new Student(id, name, marks));
            }
        } catch (IOException e) {
            // Fail safely without crashing
            System.err.println("Error loading students from file: " + e.getMessage());
        }

        return students;
    }
}
