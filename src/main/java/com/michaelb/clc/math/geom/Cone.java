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
    public double volume() {
        return PI * square(super.radius) * (super.height / 3.0);
    }

    @Override
    public double surfaceArea() {
        return PI * super.radius *
            (super.radius + root(square(super.height)) + square(super.radius));
    }
}
