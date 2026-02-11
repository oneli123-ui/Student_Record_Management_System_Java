// Author : Oneli Liyanage(10695938)
// Date : 10/2/2026
// Assignment : ASSIGNMENT 2 - CSP3341 Programming Languages and Paradigms
// File : StudentManager.java
// Functionality : Manages all student records.
// Ensures student ID format, name uniqueness, and valid mark ranges before allowing operations on student records.

package srms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// StudentManager class - Manages all student records and validates student data
public class StudentManager {

    private final ArrayList<Student> students = new ArrayList<>();
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^S\\d{3}$");

    // Constructor - Loads previously saved students from CSV file on startup
    public StudentManager() {
        this.students.addAll(FileHandler.loadStudents());
    }

    // Adds a new student after validating ID format, name uniqueness, and mark ranges
    public boolean addStudent(Student s) {
        if (s == null) return false;

        String id = safeTrim(s.getId());
        String name = safeTrim(s.getName());

        if (id.isEmpty() || name.isEmpty()) return false;
        if (!isValidStudentId(id)) return false;
        if (findById(id) != null) return false;
        if (findByName(name) != null) return false;
        if (!areMarksValid(s)) return false;

        s.setId(s.getId().trim());
        s.setName(s.getName().trim());
        students.add(s);
        return true;
    }

    // Removes a student from the system by ID
    public boolean removeStudent(String id) {
        Student s = findById(id);
        if (s == null) return false;
        return students.remove(s);
    }

    // Searches for a student by ID (case-insensitive)
    public Student findById(String id) {
        String target = safeTrim(id);
        if (target.isEmpty()) return null;

        for (Student s : students) {
            if (s.getId() != null && s.getId().equalsIgnoreCase(target)) {
                return s;
            }
        }
        return null;
    }

    // Searches for a student by name (case-insensitive)
    private Student findByName(String name) {
        String target = safeTrim(name);
        if (target.isEmpty()) return null;

        for (Student s : students) {
            if (s.getName() != null && s.getName().equalsIgnoreCase(target)) {
                return s;
            }
        }
        return null;
    }

    // Validates that student ID matches the format S### (e.g., S001, S042, S999)
    private boolean isValidStudentId(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return STUDENT_ID_PATTERN.matcher(id.trim()).matches();
    }

    // Validates that all student marks are within the acceptable range (0-100)
    private boolean areMarksValid(Student s) {
        if (s.getSubjectCount() == 0) return false;
        for (double marks : s.getAllSubjectMarks().values()) {
            if (!isMarkInRange(marks)) return false;
        }
        return true;
    }

    // Checks if a mark is within the valid range (0-100)
    private boolean isMarkInRange(double mark) {
        return mark >= 0 && mark <= 100;
    }

    // Returns validation error message for student ID, or null if valid
    public String getIdValidationError(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "Student ID cannot be empty";
        }

        String trimmedId = id.trim();
        if (!isValidStudentId(trimmedId)) {
            return "Student ID must be in format S### (e.g., S001, S042, S999)";
        }
        if (findById(trimmedId) != null) {
            return "Student ID already exists";
        }
        return null;
    }

    // Returns validation error message for student name, or null if valid
    public String getNameValidationError(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Student name cannot be empty";
        }

        String trimmedName = name.trim();
        if (findByName(trimmedName) != null) {
            return "Student name already exists";
        }
        return null;
    }

    // Returns a copy of all students in the system
    public List<Student> getAll() {
        return new ArrayList<>(students);
    }

    // Safely trims a string, returning empty string if null
    private String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }
}




