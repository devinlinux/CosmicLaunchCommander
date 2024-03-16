package com.michaelb.clc.math.geom;

public sealed abstract class Shape permits Cylinder, Cone, TruncatedCone {

    protected double radius;
    protected double height;

    public Shape(final double radius, final double height) {
        this.radius = radius;
        this.height = height;
    }

    public abstract double volume();
    public abstract double surfaceArea();

    public void radius(final double radius) { this.radius = radius; }
    public void height(final double height) { this.height = height; }

    public double radius() { return this.radius; }
    public double height() { return this.height; }
}
