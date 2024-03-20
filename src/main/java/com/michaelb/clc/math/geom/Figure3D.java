package com.michaelb.clc.math.geom;

//  imports
import java.util.Arrays;
import java.util.List;

import com.michaelb.clc.util.Logger;

public final class Figure3D {

    private final List<Shape> components;
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
            .mapToDouble(Shape::volume)
            .sum();
    }

    private double calcSurfaceArea() {
        double surfaceArea = 0.0;
        for (int i = 0; i < this.components.size(); i++) {
            final Shape component = this.components.get(i);
            if (i == 0)  //  bottom
                surfaceArea += (component.surfaceArea() - component.topSurfaceArea());
            else if (i == this.components.size() - 1)  //  top
                surfaceArea += (component.surfaceArea() - component.baseSurfaceArea());
            else  { //  middle
                try {
                    surfaceArea += (component.surfaceArea() - component.topSurfaceArea() - component.baseSurfaceArea());
                } catch (UnsupportedOperationException e) {
                    Logger.err("Cannot use %s as a middle section".formatted(component.getClass().getSimpleName()),
                                "Figure3D::calcSurfaceArea");
                    surfaceArea = -1.0;
                    break;
                }
            }
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
