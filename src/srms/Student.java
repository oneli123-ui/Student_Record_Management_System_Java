package srms;

public class Student {

    // --------------------
    // Fields
    // --------------------
    private String id;
    private String name;
    private double marks;

    // --------------------
    // Constructor
    // --------------------
    public Student(String id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    // --------------------
    // Getters and Setters
    // --------------------
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    // --------------------
    // Computed Properties
    // --------------------

    // Returns letter grade based on marks
    public String getLetterGrade() {
        if (marks >= 85) return "HD";
        if (marks >= 75) return "D";
        if (marks >= 65) return "CR";
        if (marks >= 50) return "P";
        return "F";
    }

    // Returns GPA on a 4.0 scale
    public double getGpa() {
        switch (getLetterGrade()) {
            case "HD": return 4.0;
            case "D":  return 3.0;
            case "CR": return 2.0;
            case "P":  return 1.0;
            default:   return 0.0;
        }
    }

    // Returns pass/fail status
    public boolean isPass() {
        return marks >= 50;
    }

    // --------------------
    // Display Formatting
    // --------------------

    @Override
    public String toString() {
        return String.format(
                "%-10s %-20s %6.2f   %-2s   %4.1f   %s",
                id,
                name,
                marks,
                getLetterGrade(),
                getGpa(),
                isPass() ? "PASS" : "FAIL"
        );
    }
}
