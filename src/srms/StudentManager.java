package srms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StudentManager {

    private final ArrayList<Student> students = new ArrayList<>();

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
     * Adds a student if the ID is not already in use.
     *
     * @param s student to add
     * @return true if added successfully, false if duplicate ID or invalid student
     */
    public boolean addStudent(Student s) {
        if (s == null) return false;

        String id = safeTrim(s.getId());
        if (id.isEmpty()) return false;

        if (findById(id) != null) {
            return false; // duplicate ID
        }
        s.setId(s.getId().trim());
        s.setName(s.getName().trim());

        students.add(s);
        return true;
    }

    /**
     * Edits an existing student's name and marks.
     *
     * @param id       student ID to edit
     * @param newName  new name
     * @param newMarks new marks
     * @return true if edit succeeded, false if student not found or invalid inputs
     */
    public boolean editStudent(String id, String newName, double newMarks) {
        Student s = findById(id);
        if (s == null) return false;

        String cleanedName = safeTrim(newName);
        if (cleanedName.isEmpty()) return false;

        if (newMarks < 0 || newMarks > 100) return false;

        s.setName(cleanedName);
        s.setMarks(newMarks);
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
     * Returns a copy of all students (so callers can't directly modify internal list).
     */
    public List<Student> getAll() {
        return new ArrayList<>(students);
    }

    // --------------------
    // Sorting (Sequential)
    // --------------------

    /**
     * Sorts students by name (A-Z) using normal sequential sorting.
     */
    public void sortByNameSequential() {
        students.sort(Comparator.comparing(s -> safeTrim(s.getName()).toLowerCase()));
    }

    /**
     * Sorts students by marks (highest to lowest) using normal sequential sorting.
     */
    public void sortByMarksSequential() {
        students.sort(Comparator.comparingDouble(Student::getMarks).reversed());
    }

    // --------------------
    // Sorting (Parallel)
    // --------------------

    /**
     * Sorts students by name (A-Z) using Arrays.parallelSort (concurrency-friendly).
     */
    public void sortByNameParallel() {
        Student[] arr = students.toArray(new Student[0]);
        Arrays.parallelSort(arr, Comparator.comparing(s -> safeTrim(s.getName()).toLowerCase()));

        students.clear();
        students.addAll(Arrays.asList(arr));
    }

    /**
     * Sorts students by marks (highest to lowest) using Arrays.parallelSort (concurrency-friendly).
     */
    public void sortByMarksParallel() {
        Student[] arr = students.toArray(new Student[0]);
        Arrays.parallelSort(arr, Comparator.comparingDouble(Student::getMarks).reversed());

        students.clear();
        students.addAll(Arrays.asList(arr));
    }

    // --------------------
    // Helper
    // --------------------
    private String safeTrim(String s) {
        return (s == null) ? "" : s.trim();
    }
}
