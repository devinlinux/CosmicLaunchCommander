package com.michaelb.clc.physics;

//  imports
import com.michaelb.clc.ship.Spacecraft;
import com.michaelb.clc.physics.celestial.StarSystem;
import com.michaelb.clc.physics.celestial.bodies.CelestialBody;

import static com.michaelb.clc.math.MathUtil.square;
import static com.michaelb.clc.math.MathUtil.abs;

import static com.michaelb.clc.math.MathUtil.Cartesian3D;
import static com.michaelb.clc.math.MathUtil.Spherical;

public final class TrajectoryCalculator {

    private static final double G = 6.67743e-11;

    private final Spacecraft craft;
    private final StarSystem system;

    public TrajectoryCalculator(final Spacecraft craft, final StarSystem system) {
        this.craft = craft;
        this.system = system;
    }

    public Spherical netForce() {
        double fx = 0.0;
        double fy = 0.0;
        double fz = 0.0;

        for (CelestialBody body : this.system.members()) {
            double magnitude = gravitationalForce(body);
            Spherical angles = new Cartesian3D(
                    abs(body.x() - craft.x()),
                    abs(body.y() - craft.y()),
                    abs(body.z() - craft.z())).toSpherical();

            Spherical force = new Spherical(magnitude, angles.theta(), angles.phi());
            Cartesian3D components = force.toCartesian3D();

            fx += components.x();
            fy += components.y();
            fz += components.z();
        }

        return new Cartesian3D(fx, fy, fz).toSpherical();
    }

    private double gravitationalForce(CelestialBody body) {
        return G * ((this.craft.mass() * body.mass()) / (square(body.distanceTo(this.craft.position()))));
    }
}
