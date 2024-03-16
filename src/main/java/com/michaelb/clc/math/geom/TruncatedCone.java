package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static com.michaelb.clc.math.MathUtil.square;
import static com.michaelb.clc.math.MathUtil.root;

public final class TruncatedCone extends Shape {

    private double topRadius;

    public TruncatedCone(final double baseRadius, final double topRadius, final double height) {
        super(baseRadius, height);
        this.topRadius = topRadius;
    }

    @Override
    public double volume() {
        return (1.0 / 3.0) * PI * super.height *
            (square(this.topRadius) + this.topRadius * super.radius + square(super.radius));
    }

    @Override
    public double surfaceArea() {
        return this.topSurfaceArea() + this.baseSurfaceArea() + this.lateralSurfaceArea();
    }

    private double topSurfaceArea() {
        return PI * square(this.topRadius);
    }

    private double baseSurfaceArea() {
        return PI * square(super.radius);
    }

    private double lateralSurfaceArea() {
        return PI * (super.radius + this.topRadius) * this.slantHeight();
    }

    private double slantHeight() {
        return root(square(super.radius - this.topRadius) + square(super.height));
    }
}
