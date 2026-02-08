package srms;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Student {

    private String id;
    private String name;
    private Map<String, Double> subjectMarks;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.subjectMarks = new LinkedHashMap<>();
    }

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

    public void addSubject(String subjectName, double marks) {
        subjectMarks.put(subjectName.trim(), marks);
    }

    public double getSubjectMarks(String subjectName) {
        return subjectMarks.getOrDefault(subjectName, 0.0);
    }

    public Map<String, Double> getAllSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectMarks(String subjectName, double marks) {
        if (subjectMarks.containsKey(subjectName)) {
            subjectMarks.put(subjectName, marks);
        }
    }

    public Set<String> getSubjects() {
        return subjectMarks.keySet();
    }

    public int getSubjectCount() {
        return subjectMarks.size();
    }

    public String getSubjectGrade(String subjectName) {
        double marks = getSubjectMarks(subjectName);
        return GradeUtil.getLetterGrade(marks);
    }

    public double getOverallAverage() {
        if (subjectMarks.isEmpty()) return 0.0;
        double sum = 0;
        for (double marks : subjectMarks.values()) {
            sum += marks;
        }
        return sum / subjectMarks.size();
    }

    public String getOverallGrade() {
        return GradeUtil.getLetterGrade(getOverallAverage());
    }

    public boolean isPass() {
        for (double marks : subjectMarks.values()) {
            if (marks < 50) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-25s %6.2f   %-2s   %s",
                id, name, getOverallAverage(), getOverallGrade(),
                isPass() ? "PASS" : "FAIL");
    }

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
        sb.append("Status: ").append(isPass() ? "PASS" : "FAIL").append("\n");

        return sb.toString();
    }
}
