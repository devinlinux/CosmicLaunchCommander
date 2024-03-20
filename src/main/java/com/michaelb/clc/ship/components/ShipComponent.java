package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.sci.Material;

public interface ShipComponent {

    double dryMass();
    double wetMass();

    void material(final Material material);

    String name();
    Shape geometry();
    Material material();

    void activate();
    void deactivate();

    boolean isFunctional();

    void exec(final Command cmd);
}
