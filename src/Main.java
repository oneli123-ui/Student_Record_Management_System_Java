public class Main {

    public static void main(String[] args) {

        int marks = 75;

        String grade = Grade.getLetterGrade(marks);
        double gpa = Grade.getGradePoint(grade);
        boolean pass = Grade.isPass(grade);

        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("GPA: " + gpa);
        System.out.println("Pass: " + pass);
    }
}
