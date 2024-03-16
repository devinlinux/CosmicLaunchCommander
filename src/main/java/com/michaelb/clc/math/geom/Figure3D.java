package com.michaelb.clc.math.geom;

//  imports
import java.util.Arrays;
import java.util.List;

public final class Figure3D {

    private List<Shape> components;
    private double volume;
    private double surfaceArea;

    public Figure3D(final Shape... components) {
        this.components = Arrays.asList(components);
        this.volume = this.components.stream()
            .mapToDouble(shape -> shape.volume())
            .sum();
        this.surfaceArea = this.components.stream()
            .mapToDouble(shape -> shape.surfaceArea())
            .sum();
    }

    public void addComponent(final Shape component) { this.components.add(component); }
    public void addComponents(final Shape... components) { this.components.addAll(Arrays.asList(components)); }

    public double volume() { return this.volume; }
    public double surfaceArea() { return this.surfaceArea; }
}
