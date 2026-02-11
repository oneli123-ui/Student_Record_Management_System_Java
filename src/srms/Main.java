// Author : Oneli Liyanage(10695938)
// Date : 10/2/2026
// Assignment : ASSIGNMENT 2 - CSP3341 Programming Languages and Paradigms
// File : Main.java
// Functionality : Main class with menu-driven user interface for managing student records.
// Handles user input and program flow control.

package srms;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// Main class - Entry point for the Student Record Management System with menu-driven user interface
public class Main {

    // Main method - Initializes the application, loads saved data, and manages the program flow
    static void main(String[] args) {
        System.out.println("Student Record Management System Starting...");
        System.out.println("Data will be automatically saved and loaded.\n");

        // Create StudentManager instance - this automatically loads existing data from file
        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        // Main program loop - continues until user selects exit option
        while (running) {
            printMenu();
            int choice = readInt(sc, "Choose an option (1-6): ");

            // Process user selection and route to appropriate operation
            switch (choice) {
                case 1 -> addStudentFlow(sc, manager);
                case 2 -> editStudentFlow(sc, manager);
                case 3 -> removeStudentFlow(sc, manager);
                case 4 -> viewAllStudentsMenu(sc, manager);
                case 5 -> searchByIdFlow(sc, manager);
                case 6 -> {
                    // When exiting, ensure all data is saved to CSV before closing
                    System.out.println("Saving data...");
                    FileHandler.saveStudents(manager.getAll());
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please select 1–6.");
            }
            System.out.println();
        }
        sc.close();
    }

    // Displays the main menu with all available options
    private static void printMenu() {
        System.out.println("======================================");
        System.out.println(" Student Record Management System");
        System.out.println("======================================");
        System.out.println("1) Add student");
        System.out.println("2) Edit student");
        System.out.println("3) Remove student");
        System.out.println("4) View all students");
        System.out.println("5) Search by student ID");
        System.out.println("6) Exit");
        System.out.println("--------------------------------------");
    }

    // Handles adding a new student with validated ID, name, and subject marks
    private static void addStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Add Student ----");

        // Repeatedly prompt for student ID until a valid one is provided.
        // IDs must follow the format S### (e.g., S001) and must not already exist.
        String id;
        while (true) {
            id = readNonEmpty(sc, "Enter student ID (format: S###, e.g., S001): ");
            String idError = manager.getIdValidationError(id);
            if (idError == null) break;
            System.out.println("✗ " + idError);
        }

        // Get the student's name, removing any commas to avoid CSV format issues
        String name;
        while (true) {
            name = readNonEmpty(sc, "Enter student name: ").replace(",", " ");
            String nameError = manager.getNameValidationError(name);
            if (nameError == null) break;
            System.out.println("✗ " + nameError);
        }

        // Create a new Student object with the validated ID and name
        Student s = new Student(id, name);

        // Allow the user to add multiple subjects and their corresponding marks.
        // The loop continues until the user types 'done' to exit.
        System.out.println("\nAdd subjects (enter subject name and marks):");
        System.out.println("(Type 'done' when finished adding subjects)");
        boolean addingSubjects = true;
        while (addingSubjects) {
            String subject = readNonEmpty(sc, "Enter subject name (or 'done' to finish): ");
            if (subject.equalsIgnoreCase("done")) {
                addingSubjects = false;
            } else {
                double marks = readMarks(sc, "  Enter marks for " + subject + " (0-100): ");
                s.addSubject(subject, marks);
                System.out.println("  ✓ Added: " + subject + " - " + marks);
            }
        }

        // Ensure the student has at least one subject before adding to the system
        if (s.getSubjectCount() == 0) {
            System.out.println("\n✗ Student must have at least one subject. Cancelled.");
            return;
        }

        // Attempt to add the student. If successful, display a detailed summary
        // including calculated average, grade, and GPA. Else show error message
        if (manager.addStudent(s)) {
            System.out.println("\n✓ Student added successfully!");
            System.out.println("=".repeat(80));
            System.out.println("STUDENT SUMMARY");
            System.out.println("=".repeat(80));
            System.out.println(s.toDetailedString());
            System.out.println("=".repeat(80));
        } else {
            System.out.println("\n✗ Failed to add student.");
        }
    }

    // Handles editing an existing student's name and subject marks
    private static void editStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Edit Student ----");

        // Search for the student by ID. If not found, inform the user and return
        String id = readNonEmpty(sc, "Enter student ID to edit: ");
        Student existing = manager.findById(id);

        if (existing == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }

        // Display the student's current record before making any changes
        System.out.println("\nCurrent record:");
        System.out.println(existing.toDetailedString());

        // Update the student's name
        String newName = readNonEmpty(sc, "\nEnter new name: ").replace(",", " ");
        existing.setName(newName);

        // Display current subjects so the user knows what they can modify
        System.out.println("\nEdit subjects:");
        System.out.println("Current subjects: " + String.join(", ", existing.getSubjects()));

        // Allow the user to choose between updating existing subject marks or adding new subjects.
        // The loop continues until the user selects option 3
        boolean editingSubjects = true;
        while (editingSubjects) {
            System.out.println("\n1) Update existing subject marks");
            System.out.println("2) Add new subject");
            System.out.println("3) Done editing");

            int choice = readInt(sc, "Choose option (1-3): ");

            if (choice == 1) {
                // Update marks for an existing subject
                String subject = readNonEmpty(sc, "Enter subject name to update: ");
                if (existing.getSubjects().contains(subject)) {
                    double marks = readMarks(sc, "Enter new marks (0-100): ");
                    existing.setSubjectMarks(subject, marks);
                    System.out.println("✓ Updated: " + subject + " - " + marks);
                } else {
                    System.out.println("✗ Subject not found.");
                }
            } else if (choice == 2) {
                // Add a new subject to the student's record
                String subject = readNonEmpty(sc, "Enter new subject name: ");
                double marks = readMarks(sc, "Enter marks (0-100): ");
                existing.addSubject(subject, marks);
                System.out.println("✓ Added: " + subject + " - " + marks);
            } else if (choice == 3) {
                editingSubjects = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        // Display the updated student record after all changes have been made
        System.out.println("\n✓ Student updated successfully.");
        System.out.println("\nUpdated record:");
        System.out.println(existing.toDetailedString());
    }

    // Removes a student record from the system by ID
    private static void removeStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Remove Student ----");
        String id = readNonEmpty(sc, "Enter student ID to remove: ");
        boolean removed = manager.removeStudent(id);
        System.out.println(removed ? "Student removed successfully." : "No student found with that ID.");
    }

    // Displays submenu for choosing sort method (sequential by name or parallel by ID)
    private static void viewAllStudentsMenu(Scanner sc, StudentManager manager) {
        System.out.println("---- View All Students ----");
        System.out.println("1) Sort by Name (Sequential)");
        System.out.println("2) Sort by ID (Parallel)");

        int choice = readInt(sc, "Choose sorting option (1-2): ");

        if (choice == 1) {
            viewAllStudentsByNameSequential(manager);
        } else if (choice == 2) {
            viewAllStudentsByIdParallel(manager);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // Displays all students sorted by name using sequential sorting (single-threaded)
    private static void viewAllStudentsByNameSequential(StudentManager manager) {
        System.out.println("\n---- All Students (Sequential Sort by Name) ----");

        List<Student> all = manager.getAll();
        if (all.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        // Sort students by name in alphabetical order (case-insensitive)
        all.sort(Comparator.comparing(s -> s.getName().toLowerCase()));

        // Display the sorted list in a formatted table
        printHeader();
        for (Student s : all) {
            System.out.println(s);
        }
        System.out.println("\nTotal students: " + all.size());
    }

    private static void viewAllStudentsByIdParallel(StudentManager manager) {
        System.out.println("\n---- All Students (Parallel Sort by ID) ----");

        List<Student> all = manager.getAll();
        if (all.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        // Sort students by ID using parallel streams for concurrent processing
        List<Student> sortedList = all.parallelStream()
                .sorted(Comparator.comparing(Student::getId))
                .toList();

        // Display the sorted list in a formatted table
        printHeader();
        for (Student s : sortedList) {
            System.out.println(s);
        }
        System.out.println("\nTotal students: " + sortedList.size());
    }

    // Searches for a student by ID and displays their complete details or an error message
    private static void searchByIdFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Search by Student ID ----");
        String id = readNonEmpty(sc, "Enter student ID to search: ");
        Student student = manager.findById(id);

        if (student == null) {
            System.out.println("✗ No student found with ID: " + id);
        } else {
            System.out.println("✓ Student found!\n");
            System.out.println(student.toDetailedString());
        }
    }

    // Reads an integer from user input with error handling, re-prompts if input is invalid
    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Reads a non-empty string from user input, ensuring critical fields are not left blank
    private static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    // Reads and validates student marks (0-100) from user input with numeric validation
    private static double readMarks(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                double marks = Double.parseDouble(input);
                if (marks >= 0 && marks <= 100) {
                    return marks;
                }
                System.out.println("✗ Marks must be between 0 and 100. You entered: " + marks);
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid input. Please enter a number between 0 and 100.");
            }
        }
    }

    // Prints the column header for the student list display (ID, Name, Average, Grade, Status)
    private static void printHeader() {
        System.out.printf("%-10s %-25s %6s   %-2s   %s%n",
                "ID", "Name", "Avg", "Gr", "Status");
        System.out.println("-----------------------------------------------------------------------");
    }
}
