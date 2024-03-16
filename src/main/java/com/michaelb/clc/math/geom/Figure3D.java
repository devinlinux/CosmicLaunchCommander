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
        this.recalc();
    }

    private void recalc() {
        this.volume = calcVolume();
        this.surfaceArea = calcSurfaceArea();
    }

    private double calcVolume() {
        return this.components.stream()
            .mapToDouble(shape -> shape.volume())
            .sum();
    }

    private double calcSurfaceArea() {
        double surfaceArea = 0.0;
        for (int i = 0; i < this.components.size(); i++) {
            final Shape component = this.components.get(i);
            if (i == 0)
                surfaceArea += component.surfaceArea() - component.topSurfaceArea();
            else if (i == this.components.size() - 1)
                surfaceArea += component.surfaceArea() - component.baseSurfaceArea();
            else
                surfaceArea += component.surfaceArea() - component.topSurfaceArea() - component.baseSurfaceArea();
        }
        return surfaceArea;
    }

    public void addComponent(final Shape component) {
        this.components.add(component);
        this.recalc();
    }
    public void addComponents(final Shape... components) {
       this.components.addAll(Arrays.asList(components));
       this.recalc();
    }

    public double volume() { return this.volume; }
    public double surfaceArea() { return this.surfaceArea; }
}
