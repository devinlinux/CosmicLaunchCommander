package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.sci.Material;

public abstract sealed class ShipComponent permits FuelTank {

    private String name;

    private Shape geometry;
    private Material material;

    protected ShipComponent(final String name, final Shape geometry, final Material material) {
        this.name = name;
        this.geometry = geometry;
        this.material = material;
    }

    public void geometry(final Shape geometry) { this.geometry = geometry; }
    public void material(final Material material) { this.material = material; }

    public Shape geometry() { return this.geometry; }
    public Material material() { return this.material; }
    public String name() { return this.name; }

    public abstract void activate();
    public abstract void deactivate();

    public abstract boolean isFunctional();

    public abstract void exec(final Command cmd);
}
