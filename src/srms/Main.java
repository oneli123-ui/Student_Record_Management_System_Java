package srms;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String DATA_FILE = "students.csv";

    public static void main(String[] args) {
        // 1) Load existing data from file
        List<Student> loadedStudents = FileStorage.load(DATA_FILE);
        StudentManager manager = new StudentManager(loadedStudents);

        // 2) Menu loop
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt(sc, "Choose an option (1-7): ");

            switch (choice) {
                case 1:
                    addStudentFlow(sc, manager);
                    break;
                case 2:
                    editStudentFlow(sc, manager);
                    break;
                case 3:
                    removeStudentFlow(sc, manager);
                    break;
                case 4:
                    viewAllStudents(manager);
                    break;
                case 5:
                    sortSequentialFlow(sc, manager);
                    break;
                case 6:
                    sortParallelFlow(sc, manager);
                    break;
                case 7:
                    FileStorage.save(manager.getAll(), DATA_FILE);
                    System.out.println("Saved to " + DATA_FILE + ". Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1–7.");
            }

            System.out.println(); // spacing between loops
        }

        sc.close();
    }

    // --------------------
    // Menu + UI Helpers
    // --------------------

    private static void printMenu() {
        System.out.println("======================================");
        System.out.println(" Student Record Management System");
        System.out.println("======================================");
        System.out.println("1) Add student");
        System.out.println("2) Edit student");
        System.out.println("3) Remove student");
        System.out.println("4) View all students");
        System.out.println("5) Sort students (sequential)");
        System.out.println("6) Sort students (parallel)");
        System.out.println("7) Save & Exit");
        System.out.println("--------------------------------------");
    }

    private static void addStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Add Student ----");

        String id = readNonEmpty(sc, "Enter student ID: ");
        String name = readNonEmpty(sc, "Enter student name: ").replace(",", " ");
        double marks = readMarks(sc, "Enter marks (0-100): ");

        Student s = new Student(id, name, marks);
        boolean added = manager.addStudent(s);

        if (added) {
            System.out.println("Student added successfully.");
        } else {
            System.out.println("Failed to add student. (Duplicate ID or invalid data)");
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

        System.out.println("Current record:");
        printHeader();
        System.out.println(existing);

        String newName = readNonEmpty(sc, "Enter new name: ").replace(",", " ");
        double newMarks = readMarks(sc, "Enter new marks (0-100): ");

        boolean updated = manager.editStudent(id, newName, newMarks);
        System.out.println(updated ? "Student updated successfully." : "Update failed.");
    }

    private static void removeStudentFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Remove Student ----");

        String id = readNonEmpty(sc, "Enter student ID to remove: ");
        boolean removed = manager.removeStudent(id);

        System.out.println(removed ? "Student removed successfully." : "No student found with that ID.");
    }

    private static void viewAllStudents(StudentManager manager) {
        System.out.println("---- All Students ----");

        List<Student> all = manager.getAll();
        if (all.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        printHeader();
        for (Student s : all) {
            System.out.println(s);
        }
    }

    private static void sortSequentialFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Sort (Sequential) ----");
        System.out.println("1) By name (A-Z)");
        System.out.println("2) By marks (high to low)");

        int sortChoice = readInt(sc, "Choose sort option (1-2): ");
        if (sortChoice == 1) {
            manager.sortByNameSequential();
            System.out.println("Sorted by name (sequential).");
        } else if (sortChoice == 2) {
            manager.sortByMarksSequential();
            System.out.println("Sorted by marks (sequential).");
        } else {
            System.out.println("Invalid sort option.");
        }
    }

    private static void sortParallelFlow(Scanner sc, StudentManager manager) {
        System.out.println("---- Sort (Parallel) ----");
        System.out.println("1) By name (A-Z)");
        System.out.println("2) By marks (high to low)");

        int sortChoice = readInt(sc, "Choose sort option (1-2): ");
        if (sortChoice == 1) {
            manager.sortByNameParallel();
            System.out.println("Sorted by name (parallel).");
        } else if (sortChoice == 2) {
            manager.sortByMarksParallel();
            System.out.println("Sorted by marks (parallel).");
        } else {
            System.out.println("Invalid sort option.");
        }
    }

    // --------------------
    // Input Validation
    // --------------------

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
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
                System.out.println("Marks must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric value for marks.");
            }
        }
    }

    // --------------------
    // Display Formatting
    // --------------------

    private static void printHeader() {
        System.out.println(String.format("%-10s %-20s %6s   %-2s   %4s   %s",
                "ID", "Name", "Marks", "Gr", "GPA", "Status"));
        System.out.println("----------------------------------------------------------");
    }
}
