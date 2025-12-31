public class Grade {

    public static String getLetterGrade(int marks) {
        if (marks >= 80) return "HD";
        else if (marks >= 70) return "D";
        else if (marks >= 60) return "CR";
        else if (marks >= 50) return "P";
        else return "F";
    }

    public static double getGradePoint(String letterGrade) {
        return switch (letterGrade) {
            case "HD" -> 4.0;
            case "D" -> 3.0;
            case "CR" -> 2.0;
            case "P" -> 1.0;
            default -> 0.0;
        };
    }

    public static boolean isPass(String letterGrade) {
        return !letterGrade.equals("F");
    }
}

