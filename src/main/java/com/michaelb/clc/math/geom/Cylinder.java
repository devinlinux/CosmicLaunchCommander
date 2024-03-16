package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static java.lang.Math.TAU;
import static com.michaelb.clc.math.MathUtil.square;

public final class Cylinder extends Shape {

    public Cylinder(final double radius, final double height) {
        super(radius, height);
    }

    @Override
    protected double calcVolume() {
        return PI * square(super.radius) * super.height;
    }

    @Override
    protected double calcSurfaceArea() {
        return TAU * super.radius * super.height + TAU * square(super.radius);
    }

    @Override
    protected double topSurfaceArea() { return super.surfaceArea; }

    @Override
    protected double baseSurfaceArea() { return super.surfaceArea; }
}
