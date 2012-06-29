/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 29, 2004
 * Time: 2:35:06 AM
 */
package com.technoetic.xplanner.tags.displaytag;

public class HoursDecorator {
    public static double getPercentCompletedScore(double estimatedHours,
                                  double actualHours,
                                  double remainingHours,
                                  boolean completed) {
        if (completed) return actualHours + 2;
        if (estimatedHours == 0) {
            return -1;
        } else {
            return (1 - (remainingHours / estimatedHours));
        }
    }

    public static double getRemainingHoursScore(double actualHours, double remainingHours, boolean completed) {
        if (completed) return actualHours * -1;
        return remainingHours;
    }

    public static String formatPercentDifference(double originalHours, double finalHours) {
        int error = getPercentDifference(originalHours, finalHours);
        String str = error >= 0 ? "+" : "-";
        str += Math.abs(error);
        str +='%';
        return str;
    }

    public static int getPercentDifference(double originalHours, double finalHours) {
        double delta = finalHours - originalHours;
        int error = (int)((delta/originalHours)*100);
        return error;
    }
}