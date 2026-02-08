package srms;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    static void main(String[] args) {
        System.out.println("Student Record Management System Starting...");
        System.out.println("Data stored in-memory. Changes will be lost when you exit.\n");

        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt(sc, "Choose an option (1-6): ");

            switch (choice) {
                case 1 -> addStudentFlow(sc, manager);
                case 2 -> editStudentFlow(sc, manager);
                case 3 -> removeStudentFlow(sc, manager);
                case 4 -> viewAllStudents(manager);
                case 5 -> searchByIdFlow(sc, manager);
                case 6 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please select 1–6.");
            }
            System.out.println();
        }
        sc.close();
    }

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

    private static void addStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Add Student ----");

        String id;
        while (true) {
            id = readNonEmpty(sc, "Enter student ID (format: S###, e.g., S001): ");
            String idError = manager.getIdValidationError(id);
            if (idError == null) break;
            System.out.println("✗ " + idError);
        }

        String name;
        while (true) {
            name = readNonEmpty(sc, "Enter student name: ").replace(",", " ");
            String nameError = manager.getNameValidationError(name);
            if (nameError == null) break;
            System.out.println("✗ " + nameError);
        }

        Student s = new Student(id, name);
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

        if (s.getSubjectCount() == 0) {
            System.out.println("\n✗ Student must have at least one subject. Cancelled.");
            return;
        }

        if (manager.addStudent(s)) {
            System.out.println("\n✓ Student added successfully!");
            System.out.println("=".repeat(80));
            System.out.println("STUDENT SUMMARY");
            System.out.println("=".repeat(80));
            printHeader();
            System.out.println(s);
            System.out.println("=".repeat(80));
        } else {
            System.out.println("\n✗ Failed to add student.");
        }
    }

    private static void editStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Edit Student ----");

        String id = readNonEmpty(sc, "Enter student ID to edit: ");
        Student existing = manager.findById(id);

        if (existing == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }

        System.out.println("\nCurrent record:");
        System.out.println(existing.toDetailedString());

        String newName = readNonEmpty(sc, "\nEnter new name: ").replace(",", " ");
        existing.setName(newName);

        System.out.println("\nEdit subjects:");
        System.out.println("Current subjects: " + String.join(", ", existing.getSubjects()));

        boolean editingSubjects = true;
        while (editingSubjects) {
            System.out.println("\n1) Update existing subject marks");
            System.out.println("2) Add new subject");
            System.out.println("3) Done editing");

            int choice = readInt(sc, "Choose option (1-3): ");

            if (choice == 1) {
                String subject = readNonEmpty(sc, "Enter subject name to update: ");
                if (existing.getSubjects().contains(subject)) {
                    double marks = readMarks(sc, "Enter new marks (0-100): ");
                    existing.setSubjectMarks(subject, marks);
                    System.out.println("✓ Updated: " + subject + " - " + marks);
                } else {
                    System.out.println("✗ Subject not found.");
                }
            } else if (choice == 2) {
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

        System.out.println("\n✓ Student updated successfully.");
        System.out.println("\nUpdated record:");
        System.out.println(existing.toDetailedString());
    }

    private static void removeStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Remove Student ----");
        String id = readNonEmpty(sc, "Enter student ID to remove: ");
        boolean removed = manager.removeStudent(id);
        System.out.println(removed ? "Student removed successfully." : "No student found with that ID.");
    }

    private static void viewAllStudents(StudentManager manager) {
        System.out.println("---- All Students (Sorted by Name) ----");

        List<Student> all = manager.getAll();
        if (all.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        all.sort(Comparator.comparing(s -> s.getName().toLowerCase()));
        printHeader();
        for (Student s : all) {
            System.out.println(s);
        }
        System.out.println("\nTotal students: " + all.size());
    }

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

    private static void printHeader() {
        System.out.printf("%-10s %-25s %6s   %-2s   %s%n",
                "ID", "Name", "Avg", "Gr", "Status");
        System.out.println("-----------------------------------------------------------------------");
    }
}


