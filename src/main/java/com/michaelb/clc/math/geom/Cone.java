package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static com.michaelb.clc.math.MathUtil.square;
import static com.michaelb.clc.math.MathUtil.root;

public final class Cone extends Shape {

    public Cone(final double radius, final double height) {
        super(radius, height);
    }

    @Override
    protected double calcVolume() {
        return PI * square(super.radius) * (super.height / 3.0);
    }

    @Override
    protected double calcSurfaceArea() {
        return PI * super.radius *
            (super.radius + root(square(super.height)) + square(super.radius));
    }

    @Override
    protected double topSurfaceArea() { return 0.0; }

    @Override
    protected double baseSurfaceArea() { return PI * square(super.radius); }
}
