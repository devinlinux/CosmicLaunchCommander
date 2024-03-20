package com.michaelb.clc.math.geom;

//  imports
import com.michaelb.clc.util.Logger;

public sealed abstract class Shape permits Cylinder, HollowCylinder, Cone, TruncatedCone, Sphere, SphericalCap {

    protected double radius;
    protected double height;

    protected double volume;
    protected double surfaceArea;

    protected double baseSurfaceArea;

    public Shape(final double radius, final double height) {
        if (radius <= 0.0 || height <= 0.0) {
            Logger.err("Radius and height must be positive", "Shape::new");
            throw new IllegalArgumentException("Radius and height must be positive");
        }
        this.radius = radius;
        this.height = height;

        this.recalc();
    }

    protected void recalc() {
        this.volume = this.calcVolume();
        this.surfaceArea = this.calcSurfaceArea();
        this.baseSurfaceArea = this.calcBaseSurfaceArea();
    }

    protected abstract double calcVolume();
    protected abstract double calcSurfaceArea();
    protected abstract double calcBaseSurfaceArea();

    public double volume() { return this.volume; }
    public double surfaceArea() { return this.surfaceArea; }

    protected double topSurfaceArea() throws UnsupportedOperationException { return this.baseSurfaceArea; }
    protected double baseSurfaceArea() throws UnsupportedOperationException { return this.baseSurfaceArea; }

    public void radius(final double radius) {
        this.radius = radius;
        this.recalc();
    }

    public void height(final double height) {
        this.height = height;
        this.recalc();
    }

    public double radius() { return this.radius; }
    public double height() { return this.height; }
}
