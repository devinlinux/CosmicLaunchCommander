package com.michaelb.clc.sci;

public enum Material {
    STAINLESS_STEEL(8.03);  // g/cm^3

    private final double density;

    Material(final double density) {
        this.density = density;
    }

    public double density() { return this.density; }
}
