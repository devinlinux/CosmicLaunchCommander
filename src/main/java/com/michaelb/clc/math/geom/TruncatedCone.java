package com.michaelb.clc.math.geom;

//  imports
import static java.lang.Math.PI;
import static com.michaelb.clc.math.MathUtil.square;
import static com.michaelb.clc.math.MathUtil.root;

public final class TruncatedCone extends Shape {

    private double topRadius;

    private double topSurfaceArea;

    public TruncatedCone(final double baseRadius, final double topRadius, final double height) {
        super(baseRadius, height);
        this.topRadius = topRadius;
        this.recalc();
    }

    @Override
    protected void recalc() {
        super.volume = calcVolume();
        super.surfaceArea = surfaceArea();
        this.topSurfaceArea = calcTopSurfaceArea();
        super.baseSurfaceArea = calcBaseSurfaceArea();
    }

    @Override
    protected double calcVolume() {
        return (1.0 / 3.0) * PI * super.height *
            (square(this.topRadius) + this.topRadius * super.radius + square(super.radius));
    }

    @Override
    protected double calcSurfaceArea() {
        return this.topSurfaceArea() + this.baseSurfaceArea() + this.lateralSurfaceArea();
    }

    @Override
    protected double topSurfaceArea() throws UnsupportedOperationException { return this.topSurfaceArea; }

    private double calcTopSurfaceArea() {
        return PI * square(this.topRadius);
    }

    @Override
    protected double calcBaseSurfaceArea() {
        return PI * square(super.radius);
    }

    private double lateralSurfaceArea() {
        return PI * (super.radius + this.topRadius) * this.slantHeight();
    }

    private double slantHeight() {
        return root(square(super.radius - this.topRadius) + square(super.height));
    }

    public void topRadius(final double topRadius) {
        this.topRadius = topRadius;
        this.recalc();
    }

    public double topRadius() { return this.topRadius; }
}
