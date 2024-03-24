package com.michaelb.clc.gui.components;

//  imports
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Component;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class AdvancedButton extends JPanel implements MouseListener {

    private final String text;
    private final Font font;

    private final Color foreground;
    private final PanelAnimation defaultAnimation;
    private final PanelAnimation hoverAnimation;

    private Dimension dimension;

    private final ActionListener listener;

    private boolean hovered;

    public AdvancedButton(final String text, final Font font, final Color foreground,
            final PanelAnimation defaultAnimation, final PanelAnimation hoverAnimation,
            final Dimension dimension, final ActionListener listener) {
        this.text = text;
        this.font = font;

        this.foreground = foreground;
        this.defaultAnimation = defaultAnimation;
        this.hoverAnimation = hoverAnimation;

        this.dimension = dimension;

        this.listener = listener;

        this.hovered = false;
    }

    private void createButton() {
        this.setLayout(new GridBagLayout());

        this.setMinimumSize(this.dimension);
        this.setPreferredSize(this.dimension);
        this.setMaximumSize(this.dimension);

        JLabel buttonText = new JLabel(this.text);
        buttonText.setForeground(this.foreground);
        buttonText.setOpaque(false);

        this.add(buttonText);
        this.setVisible(true);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.hovered)
            this.hoverAnimation.draw(g, this.getWidth(), this.getHeight());
        else
            this.defaultAnimation.draw(g, this.getWidth(), this.getHeight());
    }

    public static interface PanelAnimation {
        void draw(Graphics g, int width, int height);
    }

    /* mouse listener */

    @Override
    public void mouseClicked(MouseEvent e) {
        listener.actionPerformed(null);
        //  might need interface here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hovered = true;
        //  maybe repaint
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hovered = false;
        //  maybe repaint
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // set background white or something
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //  undo backgroud set
    }
}
