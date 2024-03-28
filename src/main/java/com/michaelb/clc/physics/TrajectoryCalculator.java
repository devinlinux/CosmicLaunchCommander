package com.michaelb.clc.physics;

//  imports
import com.michaelb.clc.ship.Spacecraft;
import com.michaelb.clc.physics.celestial.StarSystem;

import static com.michaelb.clc.math.MathUtil.root;
import static com.michaelb.clc.math.MathUtil.square;

public final class TrajectoryCalculator {

    private static final double G = 6.67743e-11;

    private final Spacecraft craft;
    private final StarSystem system;

    public TrajectoryCalculator(final Spacecraft craft, final StarSystem system) {
        this.craft = craft;
        this.system = system;
    }

    public Force netForce() {

    }
}
