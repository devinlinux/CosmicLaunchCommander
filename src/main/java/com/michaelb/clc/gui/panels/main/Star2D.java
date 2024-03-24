package com.michaelb.clc.gui.panels.main;

//  imports
import java.util.Random;

import java.awt.Graphics;
import java.awt.Color;

public final class Star2D {

    public int x;
    public int y;

    private final int size;

    private final int fadeDuration;
    private int fadeStep = 0;

    private boolean fadingIn;
    private boolean fadingOut;

    private final Random rand;

    public Star2D(final int x, final int y, final int size, final int fadeDuration) {
        this.x = x;
        this.y = y;

        this.size = size;

        this.fadeDuration = fadeDuration;
        this.fadeStep = 0;

        this.fadingIn = false;
        this.fadingOut = false;

        this.rand = new Random();
    }

    public void update() {
        if (this.fadingOut) {
            this.fadeStep--;
            if (this.fadeStep <= 0) {
                this.fadingOut = false;
                this.fadingIn = true;
                this.fadeStep = 0;
            }
        } else if (this.fadingIn) {
            this.fadeStep++;
            if (this.fadeStep >= this.fadeDuration) {
                this.fadingIn = false;
                this.fadeStep = this.fadeDuration;
            }
        } else {
            if (rand.nextInt(100) < 2)  //  2% chance of fading
                fadingOut = true;
        }
    }

    public void draw(Graphics g) {
        int alpha;
        if (this.fadingIn)
            alpha = 255 * this.fadeStep / this.fadeDuration;
        else if (this.fadingOut)
            alpha = (int) (255 * ((double) this.fadeStep / this.fadeDuration));
        else
            alpha = 255;

        g.setColor(new Color(255, 255, 255, alpha));
        g.fillOval(x - size / 2, y - size / 2, size, size);
    }
}
