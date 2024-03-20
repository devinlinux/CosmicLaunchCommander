package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Sphere;
import com.michaelb.clc.sci.Material;

public final class HeaderTank extends FuelTank {
    public HeaderTank(final String name, final double radius, final Material material) {
        super(name, new Sphere(radius), material);
    }
}
