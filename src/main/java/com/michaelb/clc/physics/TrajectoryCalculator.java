package com.michaelb.clc.physics;

//  imports
import com.michaelb.clc.physics.celestial.StarSystem;

public final class TrajectoryCalculator {

    private final StarSystem system;

    public TrajectoryCalculator(StarSystem system) {
        this.system = system;
    }

    private static final double G = 6.67743e-11;
}
