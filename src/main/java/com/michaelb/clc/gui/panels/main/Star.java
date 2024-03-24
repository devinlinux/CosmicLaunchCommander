package com.michaelb.clc.gui.panels.main;

public class Star {

    private final int x;
    private final int y;

    private final int size;

    private final int fadeDuration;
    private int fadeStep = 0;
    private boolean fading;

    public Star(final int x, final int y, final int size, final int fadeDuration) {
        this.x = x;
        this.y = y;

        this.size = size;

        this.fadeDuration = fadeDuration;
        this.fadeStep = 0;
        this.fading = false;
    }

    public void update() {
        if (fading) {
            fadeStep++;
        }
    }
}
