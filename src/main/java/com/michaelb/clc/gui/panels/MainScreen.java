package com.michaelb.clc.gui.panels;

//  imports
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class MainScreen extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.BLACK;

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(BACKGROUND_COLOR);
        g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
