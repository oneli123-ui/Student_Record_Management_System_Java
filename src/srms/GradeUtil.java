// Author : Oneli Liyanage(10695938)
// Date : 10/2/2026
// Assignment : ASSIGNMENT 2 - CSP3341 Programming Languages and Paradigms
// File : GradeUtil.java
// Functionality : Provides utility methods for converting marks to letter grades, calculating GPA, and determining pass/fail status.

package srms;

// GradeUtil class - Utility class providing grading logic (static methods only, cannot be instantiated)
public class GradeUtil {

    // Private constructor - Prevents instantiation of utility class
    private GradeUtil() {
    }

    // Converts numeric marks (0-100) to letter grades (HD, D, CR, P, F)
    public static String getLetterGrade(double marks) {
        if (marks >= 80) return "HD";
        if (marks >= 70) return "D";
        if (marks >= 60) return "CR";
        if (marks >= 50) return "P";
        return "F";
    }

    // Converts letter grades to GPA on a 4.0 scale (HD=4.0, D=3.0, CR=2.0, P=1.0, F=0.0)
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

    // Determines if a student passes based on letter grade (anything except F is a pass)
    public static boolean isPass(String letterGrade) {
        return !letterGrade.equals("F");
    }

    // Determines if a student passes based on numeric marks (50 or above is a pass)
    public static boolean isPass(double marks) {
        return marks >= 50;
    }
}
