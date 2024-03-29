package com.michaelb.clc.physics;

//  imports
import com.michaelb.clc.ship.Spacecraft;
import com.michaelb.clc.physics.celestial.StarSystem;
import com.michaelb.clc.physics.celestial.bodies.CelestialBody;

import static com.michaelb.clc.math.MathUtil.root;
import static com.michaelb.clc.math.MathUtil.square;

import static com.michaelb.clc.math.MathUtil.tan;
import static com.michaelb.clc.math.MathUtil.sin;
import static com.michaelb.clc.math.MathUtil.cos;
import static com.michaelb.clc.math.MathUtil.arctan;
import static com.michaelb.clc.math.MathUtil.arcsin;
import static com.michaelb.clc.math.MathUtil.arccos;

public final class TrajectoryCalculator {

    private static final double G = 6.67743e-11;

    private final Spacecraft craft;
    private final StarSystem system;

    public TrajectoryCalculator(final Spacecraft craft, final StarSystem system) {
        this.craft = craft;
        this.system = system;
    }

    public Force3D netForce() {
        double fx = 0.0;
        double fy = 0.0;
        double fz = 0.0;

        for (CelestialBody body : this.system.members()) {
            double theta = 0;
        }
        return null;
    }

    private double gravitationalForce(CelestialBody body) {
        return G * ((this.craft.mass() * body.mass()) / (square(body.distanceTo(this.craft.position()))));
    }
}
