// Author : Oneli Liyanage(10695938)
// Date : 10/2/2026
// Assignment : ASSIGNMENT 2 - CSP3341 Programming Languages and Paradigms
// File : Student.java

package srms;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// Student class - Represents a student entity with ID, name, and subject marks
public class Student {

    private String id;
    private String name;
    private Map<String, Double> subjectMarks;

    // Constructor - Initializes student with ID and name, creates empty subject map
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.subjectMarks = new LinkedHashMap<>();
    }

    // Getters and setters for ID and name
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

    // Adds a subject with its marks to the student's record
    public void addSubject(String subjectName, double marks) {
        subjectMarks.put(subjectName.trim(), marks);
    }

    // Returns the marks for a specific subject, or 0.0 if not found
    public double getSubjectMarks(String subjectName) {
        return subjectMarks.getOrDefault(subjectName, 0.0);
    }

    // Returns the complete map of all subjects and their marks
    public Map<String, Double> getAllSubjectMarks() {
        return subjectMarks;
    }

    // Updates the marks for an existing subject
    public void setSubjectMarks(String subjectName, double marks) {
        if (subjectMarks.containsKey(subjectName)) {
            subjectMarks.put(subjectName, marks);
        }
    }

    // Returns the set of all subject names for this student
    public Set<String> getSubjects() {
        return subjectMarks.keySet();
    }

    // Returns the total number of subjects the student is enrolled in
    public int getSubjectCount() {
        return subjectMarks.size();
    }

    // Gets the letter grade for a specific subject based on its marks
    public String getSubjectGrade(String subjectName) {
        double marks = getSubjectMarks(subjectName);
        return GradeUtil.getLetterGrade(marks);
    }

    // Calculates the overall average mark across all subjects
    public double getOverallAverage() {
        if (subjectMarks.isEmpty()) return 0.0;
        double sum = 0;
        for (double marks : subjectMarks.values()) {
            sum += marks;
        }
        return sum / subjectMarks.size();
    }

    // Gets the letter grade based on overall average
    public String getOverallGrade() {
        return GradeUtil.getLetterGrade(getOverallAverage());
    }

    // Calculates GPA on a 4.0 scale based on the overall grade
    public double getGPA() {
        return GradeUtil.getGpa(getOverallGrade());
    }

    // Determines if the student has passed (average >= 50)
    public boolean isPass() {
        return getOverallAverage() >= 50;
    }

    // Returns a formatted summary of student: ID, Name, Average, Grade, Status
    @Override
    public String toString() {
        return String.format("%-10s %-25s %6.2f   %-2s   %s",
                id, name, getOverallAverage(), getOverallGrade(),
                isPass() ? "PASS" : "FAIL");
    }

    // Returns a detailed student record with all subjects, marks, grades, GPA, and status
    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Student Details ---\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("\nSubjects:\n");

        for (String subject : subjectMarks.keySet()) {
            double marks = subjectMarks.get(subject);
            String grade = getSubjectGrade(subject);
            sb.append("  ").append(subject).append(": ").append(marks)
                    .append(" (").append(grade).append(")\n");
        }

        sb.append("\nOverall Average: ").append(String.format("%.2f", getOverallAverage())).append("\n");
        sb.append("Overall Grade: ").append(getOverallGrade()).append("\n");
        sb.append("GPA: ").append(String.format("%.1f", getGPA())).append("\n");
        sb.append("Status: ").append(isPass() ? "PASS" : "FAIL").append("\n");

        return sb.toString();
    }
}
