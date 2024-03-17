package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static com.michaelb.clc.math.MathUtil.square;

public final class SphericalCap extends Shape {

    public SphericalCap(final double radius, final double height) {
        super(radius, height);
    }

    @Override
    protected double calcVolume() {
        return (1.0 / 6.0) * PI * super.height *
            (3 * square(super.radius) + square(super.height));
    }

    @Override
    protected double calcSurfaceArea() {
        return PI * (square(super.radius) + square(super.height));
    }

    @Override
    protected double calcBaseSurfaceArea() {
        return PI * square(super.radius);
    }

    @Override
    public double topSurfaceArea() {
        throw new UnsupportedOperationException("A Spherical Cap does not have a top surface area");
    }
}
