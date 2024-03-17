package com.michaelb.clc.math.geom;

public sealed abstract class Shape permits Cylinder, HollowCylinder, Cone, TruncatedCone, Sphere {

    protected double radius;
    protected double height;

    protected double volume;
    protected double surfaceArea;

    protected double baseSurfaceArea;

    public Shape(final double radius, final double height) {
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

    protected double topSurfaceArea() { return this.baseSurfaceArea; }
    protected double baseSurfaceArea() { return this.baseSurfaceArea; }

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
