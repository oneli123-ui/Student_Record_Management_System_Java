package srms;

/**
 * Utility class that provides grading-related logic.
 * This class is stateless and contains only static methods.
 */
public class GradeUtil {

    // Prevent instantiation (utility class)
    private GradeUtil() {
    }

    /**
     * Determines the letter grade based on marks.
     *
     * @param marks Student marks (0–100)
     * @return Letter grade (HD, D, CR, P, F)
     */
    public static String getLetterGrade(double marks) {
        if (marks >= 85) return "HD";
        if (marks >= 75) return "D";
        if (marks >= 65) return "CR";
        if (marks >= 50) return "P";
        return "F";
    }

    /**
     * Converts a letter grade to GPA on a 4.0 scale.
     *
     * @param letterGrade Letter grade (HD, D, CR, P, F)
     * @return GPA value
     */
    public static double getGpa(String letterGrade) {
        switch (letterGrade) {
            case "HD":
                return 4.0;
            case "D":
                return 3.0;
            case "CR":
                return 2.0;
            case "P":
                return 1.0;
            default:
                return 0.0;
        }
    }

    /**
     * Determines whether a student has passed based on letter grade.
     *
     * @param letterGrade Letter grade
     * @return true if pass, false otherwise
     */
    public static boolean isPass(String letterGrade) {
        return !letterGrade.equals("F");
    }

    /**
     * Alternative pass/fail check directly from marks.
     *
     * @param marks Student marks
     * @return true if marks >= 50
     */
    public static boolean isPass(double marks) {
        return marks >= 50;
    }
}
