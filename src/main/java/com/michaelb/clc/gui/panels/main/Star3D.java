package com.michaelb.clc.gui.panels.main;

//  imports
import java.awt.Graphics;
import java.awt.Color;

public final class Star3D {

    public double x;
    public double y;
    public double z;
    private final double speed;

    public Star3D(final double x, final double y, final double z, final double speed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
    }

    public void update(final double width, final double height) {
        if (Math.random() < 0.002)
            this.z -= this.speed;
        if (this.z <= 0) {
            this.z = width;
            this.x = Math.random() * width - width / 2;
            this.y = Math.random() * height - height / 2;
        }
    }

    public void draw(final Graphics g, final double x0, final double y0, final double width, final double height) {
        double sx = map(x / z, 0, 1, 0, width);
        double sy = map(y / z, 0, 1, 0, height);
        double r = map(z, 0, width, 8, 0);

        g.setColor(Color.WHITE);
        g.fillOval((int) (sx + x0 - r / 2), (int) (sy + y0 - r / 2), (int) r, (int) r);
    }

    private static double map(final double value, final double start1, final double stop1,
            final double start2, final double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }
}
