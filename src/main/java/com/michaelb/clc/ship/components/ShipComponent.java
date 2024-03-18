package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.sci.Material;

public interface ShipComponent {

    void material(final Material material);

    Shape geometry();
    Material material();
    String name();

    void activate();
    void deactivate();

    boolean isFunctional();

    void exec(final Command cmd);
}
