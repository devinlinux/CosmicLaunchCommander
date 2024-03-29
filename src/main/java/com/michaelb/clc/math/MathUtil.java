package com.michaelb.clc.math;

/* Math methods for convenience */
public final class MathUtil {
    private MathUtil() {}

    public static double square(double base) {
        return Math.pow(base, 2);
    }

    public static double cube(double base) {
        return Math.pow(base, 3);
    }

    public static double inv(double base) {
        return Math.pow(base, -1);
    }

    public static double root(double n) {
        return Math.sqrt(n);
    }

    public static double tan(double theta) {
        return Math.tan(Math.toRadians(theta));
    }

    public static double sin(double theta) {
        return Math.sin(Math.toRadians(theta));
    }

    public static double cos(double theta) {
        return Math.cos(Math.toRadians(theta));
    }

    public static double arctan(double theta) {
        return Math.atan(Math.toRadians(theta));
    }

    public static double arcsin(double theta) {
        return Math.asin(Math.toRadians(theta));
    }

    public static double arccos(double theta) {
        return Math.acos(Math.toRadians(theta));
    }
}
