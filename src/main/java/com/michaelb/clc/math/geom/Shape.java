package com.michaelb.clc.math.geom;

public sealed abstract class Shape permits Cylinder, Cone, TruncatedCone {

    protected double radius;
    protected double height;

    protected double volume;
    protected double surfaceArea;

    public Shape(final double radius, final double height) {
        this.radius = radius;
        this.height = height;

        this.recalc();
    }

    protected final void recalc() {
        this.volume = this.calcVolume();
        this.surfaceArea = this.calcSurfaceArea();
    }

    protected abstract double calcVolume();
    protected abstract double calcSurfaceArea();

    public double volume() { return this.volume; }
    public double surfaceArea() { return this.surfaceArea; }

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
