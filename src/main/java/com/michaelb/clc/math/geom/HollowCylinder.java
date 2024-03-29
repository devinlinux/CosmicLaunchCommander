package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static java.lang.Math.TAU;
import static com.michaelb.clc.math.MathUtil.square;

public final class HollowCylinder extends Shape {

    private double thickness;

    public HollowCylinder(final double radius, final double height, final double thickness) {
        super(radius, height);
        this.thickness = thickness;
        super.recalc();
    }

    @Override
    protected double calcVolume() {
        return PI * (square(this.thickness) - (2 * this.thickness * super.radius) * super.height) +
            TAU * square(super.radius - 2 * thickness) * this.thickness;  //  top and bottom
    }

    @Override
    protected double calcSurfaceArea() {
        return TAU * super.radius * super.height + TAU * square(super.radius);
    }

    @Override
    protected double calcBaseSurfaceArea() {
        return PI * square(super.radius);
    }

    public void thickness(final double thickness) {
        this.thickness = thickness;
        this.recalc();
    }

    public double thickness() { return this.thickness; }
}
