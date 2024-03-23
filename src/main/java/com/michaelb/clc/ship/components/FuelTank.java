package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.math.geom.Cylinder;
import com.michaelb.clc.sci.Material;

public sealed class FuelTank implements ShipComponent permits HeaderTank {

    private final String name;
    private final Shape geometry;
    private Material material;

    private boolean canDraw;

    private double inventory;
    private final double capacity;

    public FuelTank(final String name, final double radius, final double height, final Material material) {
        this(name, new Cylinder(radius, height), material);
    }

    public FuelTank(final String name, final Shape geometry, final Material material) {
        this.name =name;
        this.geometry = geometry;
        this.material = material;

        this.canDraw = true;
        this.capacity = (this.inventory = this.geometry().volume());
    }

    @Override
    public double dryMass() {
        return this.geometry().surfaceArea() * this.material().density();  //  FIXME: need unit conversion to go from m^2 to m^3
    }

    @Override
    public double wetMass() {
        return this.dryMass() + this.capacity * this.material().density();  //  FIXME: use fuel instead of material, units for capacity
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

    public boolean poll(final double amount) { return this.inventory >= amount; }

    protected double draw(final double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot draw negative amount from fuel tank");
        } else if (this.inventory >= amount) {
            this.inventory -= amount;
            return amount;
        } else {
            double got = this.inventory;
            this.inventory = 0;
            return got;
        }
    }

    protected double add(final double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative amount to fuel tank");
        } else if (amount > this.capacity - this.inventory) {
            double room = this.capacity - this.inventory;
            this.inventory = this.capacity;
            double surplus = amount - room;
            return surplus;
        } else {
            this.inventory += amount;
            return 0;
        }
    }

    @Override
    public void material(Material material) { this.material = material; }

    @Override
    public String name() { return this.name; }

    @Override
    public Material material() { return this.material; }

    @Override
    public Shape geometry() { return this.geometry; }

    public double capacity() { return this.geometry().volume(); }
    public double inventory() { return this.inventory; }
    public double percentRemaining() { return this.inventory / this.capacity; }
}
