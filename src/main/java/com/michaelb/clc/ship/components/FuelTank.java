package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Cylinder;
import com.michaelb.clc.sci.Material;;

public final class FuelTank extends ShipComponent {

    private boolean canDraw;

    private double inventory;
    private final double capacity;

    public FuelTank(final String name, final double radius, final double height, final Material material) {
        super(name, new Cylinder(radius, height), material);

        this.canDraw = true;
        this.capacity = (this.inventory = super.geometry().volume());
    }

    @Override
    public void activate() { this.canDraw = true; }

    @Override
    public void deactivate() { this.canDraw = false; }

    @Override
    public boolean isFunctional() {
        return this.canDraw && this.inventory > 0.0;
    }

    @Override
    public void exec(Command cmd) { 
        //  TODO: commands
    }

    public double capacity() { return super.geometry().volume(); }
    public double inventory() { return this.inventory; }
    public double percentRemaining() { return this.inventory / this.capacity; }
}
