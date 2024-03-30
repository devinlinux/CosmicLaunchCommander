package com.michaelb.clc.math;

public final class MathUtil {
    private MathUtil() {}

    /* Math methods for convenience */
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

    public static double abs(double n) {
        return Math.abs(n);
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
        return Math.atan(theta);
    }

    public static double arcsin(double theta) {
        return Math.asin(theta);
    }

    public static double arccos(double theta) {
        return Math.acos(theta);
    }

    /* coordinate conversions  */
    public record Rectangular(double x, double y) {
        public Polar toPolar() {
            return new Polar(
                    root(square(this.x) + square(this.y)),
                    Math.toDegrees(arctan(this.y / this.x)));
        }
    }

    public record Cartesian3D(double x, double y, double z) {
        public Spherical toSpherical() {
            return new Spherical(
                    root(square(this.x) + square(this.y) + square(this.z)),
                    this.z == 0 ? 0 : Math.toDegrees(arccos(z / root(square(this.x) + square(this.y) + square(this.z)))),
                    this.x  == 0 || this.y == 0 ? 0 : Math.toDegrees(arctan(this.y / this.x)));
        }
    }

    public record Polar(double r, double theta) {
        public Rectangular toRectangular() {
            return new Rectangular(
                this.r * cos(theta),
                this.r * sin(theta));
        }
    }

    public record Spherical(double r, double theta, double phi) {
        public Cartesian3D toCartesian3D() {
            return new Cartesian3D(r * sin(this.theta) * cos(this.phi),
                                   r * sin(this.theta) * sin(this.phi),
                                   r * cos(theta));
        }
    }
}
