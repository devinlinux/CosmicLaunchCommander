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
    public double calcVolume() {
        return PI * square(super.radius) * super.height;
    }

    @Override
    public double calcSurfaceArea() {
        return TAU * super.radius * super.height + TAU * square(super.radius);
    }
}
