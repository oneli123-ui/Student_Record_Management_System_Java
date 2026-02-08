package srms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StudentManager {

    private final ArrayList<Student> students = new ArrayList<>();

    // Validation constants
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^S\\d{3}$");
    // Format: S followed by exactly 3 digits (e.g., S001, S123, S999)

    // --------------------
    // Constructors
    // --------------------
    public StudentManager() {
    }

    public StudentManager(List<Student> initialStudents) {
        if (initialStudents != null) {
            students.addAll(initialStudents);
        }
    }

    // --------------------
    // Core Operations
    // --------------------

    /**
     * Adds a student if the ID is not already in use and passes validation.
     *
     * @param s student to add
     * @return true if added successfully, false if duplicate ID/name or invalid student
     */
    public boolean addStudent(Student s) {
        if (s == null) return false;

        String id = safeTrim(s.getId());
        String name = safeTrim(s.getName());

        // Validate ID is not empty
        if (id.isEmpty() || name.isEmpty()) return false;

        // Validate ID format (S001, S002, etc.)
        if (!isValidStudentId(id)) {
            return false;
        }

        // Check for duplicate ID
        if (findById(id) != null) {
            return false;
        }

        // Check for duplicate name (optional - prevents same name)
        if (findByName(name) != null) {
            return false;
        }

        // Validate all marks are in range 0-100
        if (!areMarksValid(s)) {
            return false;
        }

        s.setId(s.getId().trim());
        s.setName(s.getName().trim());

        students.add(s);
        return true;
    }

    /**
     * Removes a student by ID.
     *
     * @param id student ID
     * @return true if removed, false if not found
     */
    public boolean removeStudent(String id) {
        Student s = findById(id);
        if (s == null) return false;
        return students.remove(s);
    }

    /**
     * Finds a student by ID.
     *
     * @param id student ID
     * @return Student if found, otherwise null
     */
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

    /**
     * Finds a student by name (case-insensitive).
     *
     * @param name student name
     * @return Student if found, otherwise null
     */
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

    // --------------------
    // Validation Methods
    // --------------------

    /**
     * Validates if student ID matches the required format (S###).
     * Examples: S001, S012, S999
     *
     * @param id student ID to validate
     * @return true if valid format, false otherwise
     */
    private boolean isValidStudentId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return STUDENT_ID_PATTERN.matcher(id.trim()).matches();
    }

    /**
     * Validates that all subject marks are within 0-100 range.
     *
     * @param s student to validate
     * @return true if all marks are valid, false otherwise
     */
    private boolean areMarksValid(Student s) {
        if (s.getSubjectCount() == 0) {
            return false; // Must have at least one subject
        }

        for (double marks : s.getAllSubjectMarks().values()) {
            if (!isMarkInRange(marks)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a single mark is within valid range.
     *
     * @param mark mark to validate
     * @return true if mark is between 0 and 100 (inclusive)
     */
    private boolean isMarkInRange(double mark) {
        return mark >= 0 && mark <= 100;
    }

    /**
     * Returns detailed validation error message for a student ID.
     *
     * @param id student ID to check
     * @return error message, or null if valid
     */
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

        return null; // Valid
    }

    /**
     * Returns detailed validation error message for a student name.
     *
     * @param name student name to check
     * @return error message, or null if valid
     */
    public String getNameValidationError(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Student name cannot be empty";
        }

        String trimmedName = name.trim();

        if (findByName(trimmedName) != null) {
            return "Student name already exists";
        }

        return null; // Valid
    }

    /**
     * Returns a copy of all students (so callers can't directly modify internal list).
     */
    public List<Student> getAll() {
        return new ArrayList<>(students);
    }

    // --------------------
    // Helper
    // --------------------
    private String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }
}


