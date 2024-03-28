package com.michaelb.clc.physics.celestial.bodies;

//  imports
import com.michaelb.clc.physics.util.Position3D;

public abstract class CelestialBody {

    private final String name;
    private final double mass;
    private final double radius;

    private final Position3D position;

    private double velocity;

    public CelestialBody(final String name, final double mass, final double radius, final double x, final double y, final double z, final double velocity) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = new Position3D(x, y, z);
    }

    public String name() { return this.name; }
    public double mass() { return this.mass; }
    public double radius() { return this.radius; }

    public Position3D position() { return this.position; }
    public double x() { return this.position.x; }
    public double y() { return this.position.y; }
    public double z() { return this.position.z; }

    public double velocity() { return this.velocity; }
}
