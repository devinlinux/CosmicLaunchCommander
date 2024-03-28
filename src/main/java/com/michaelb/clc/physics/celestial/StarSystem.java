package com.michaelb.clc.physics.celestial;

//  imports
import java.util.List;
import java.util.Arrays;

import com.michaelb.clc.physics.celestial.bodies.CelestialBody;

public final class StarSystem {

    private final String name;
    private final List<CelestialBody> members;

    public StarSystem(final String name, CelestialBody... members) {
        this.name = name;
        this.members = Arrays.asList(members);
    }

    public String name() { return this.name; }
    public List<CelestialBody> members() { return this.members; }
}
