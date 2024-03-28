package com.michaelb.clc.physics.celestial.bodies;

public abstract class CelestialBody {

    private final String name;
    private final double mass;
    private final double radius;

    public CelestialBody(final String name, final double mass, final double radius) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
    }

    public String name() { return this.name; }
    public double mass() { return this.mass; }
    public double radius() { return this.radius; }
}
