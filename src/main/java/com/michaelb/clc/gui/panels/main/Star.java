package com.michaelb.clc.gui.panels.main;

//  imports
import java.util.Random;

import java.awt.Graphics;

public class Star {

    private final int x;
    private final int y;

    private final int size;

    private final int fadeDuration;
    private int fadeStep = 0;
    private boolean fading;

    private Random rand;

    public Star(final int x, final int y, final int size, final int fadeDuration) {
        this.x = x;
        this.y = y;

        this.size = size;

        this.fadeDuration = fadeDuration;
        this.fadeStep = 0;
        this.fading = false;

        this.rand = new Random();
    }

    public void update() {
        if (fading) {
            fadeStep++;
            if (fadeStep >= fadeDuration) {
                fading = false;
                fadeStep = 0;
            }
        } else {
            if (rand.nextInt(100) < 2) {
                fading = true;
            }
        }
    }
}
