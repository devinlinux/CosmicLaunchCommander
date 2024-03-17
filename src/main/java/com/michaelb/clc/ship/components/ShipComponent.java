package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.sci.Material;

public abstract class ShipComponent {

    private Shape geometry;
    private Material material;

    public void geometry(final Shape geometry) { this.geometry = geometry; }
    public void material(final Material material) { this.material = material; }

    public Shape geometry() { return this.geometry; }
    public Material material() { return this.material; }
}
