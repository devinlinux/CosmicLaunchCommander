package com.michaelb.clc.ship.components;

//  imports
import com.michaelb.clc.math.geom.Shape;
import com.michaelb.clc.math.geom.Cylinder;
import com.michaelb.clc.sci.Material;
import com.michaelb.clc.util.Logger;

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
            Logger.err("Cannot draw negative amount from fuel tank", "FuelTank::draw");
            throw new IllegalArgumentException("Cannot draw negative amount from fuel tank");
        } else if (this.inventory >= amount) {
            this.inventory -= amount;
            Logger.info("Drew %f from %s".formatted(amount, this.name()), "FuelTank::draw");
            return amount;
        } else {
            double got = this.inventory;
            Logger.info("Drew %.3f from %s".formatted(got, this.name()), "FuelTank::draw");
            this.inventory = 0;
            return got;
        }
    }

    protected double add(final double amount) {
        if (amount < 0) {
            Logger.err("Cannot add negative amount to fuel tank", "FuelTank::add");
            throw new IllegalArgumentException("Cannot add negative amount to fuel tank");
        } else if (amount > this.capacity - this.inventory) {
            double room = this.capacity - this.inventory;
            this.inventory = this.capacity;
            double surplus = amount - room;
            Logger.info("Refilled %s with %.3f surplus: ".formatted(this.name(), surplus), "FuelTank::add");
            return surplus;
        } else {
            this.inventory += amount;
            Logger.info("Added %.2f to %s".formatted(amount, this.name()), "FuelTank::add");
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
