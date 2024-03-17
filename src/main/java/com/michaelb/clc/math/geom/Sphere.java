package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static com.michaelb.clc.math.MathUtil.square;
import static com.michaelb.clc.math.MathUtil.cube;

public final class Sphere extends Shape {

    public Sphere(final double radius) {
        super(radius, 2 * radius);
    }

    @Override
    protected void recalc() {
        super.volume = this.calcVolume();
        super.surfaceArea = this.calcSurfaceArea();
    }

    @Override
    protected double calcVolume() {
        return (4.0 / 3.0) * PI * cube(super.radius);
    }

    @Override
    protected double calcSurfaceArea() {
        return 4 * PI * square(super.radius);
    }

    @Override
    protected double calcBaseSurfaceArea() {
        return 0.0;
    }

    @Override
    public double topSurfaceArea() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("A Sphere does not have a top surface area");
    }

    @Override
    public double baseSurfaceArea() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("A Sphere does not have a base surface area");
    }
}
