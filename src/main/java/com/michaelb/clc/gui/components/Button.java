package com.michaelb.clc.gui.components;

//  imports
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.event.ActionListener;

public class Button extends JPanel {

    private static final Dimension DEFAULT_DIMENSION = new Dimension(250, 50);

    private String text;
    private Font font;

    private Color foreground;
    private Color background;

    private Dimension dimension;
    private ActionListener listener;

    public Button(String text, Font font, Color foreground, Color background) {
        this(text, font, foreground, background, DEFAULT_DIMENSION, null);
    }

    public Button(String text, Color foreground, Color background) {
        this(text, new JButton().getFont(), foreground, background, DEFAULT_DIMENSION, null);
    }

    public Button(String text, Font font, Color foreground, Color background, Dimension dimension) {
        this(text, font, foreground, background, dimension, null);
    }

    public Button(String text, Font font, Color foreground, Color background, Dimension dimension, ActionListener listener) {
        this.text = text;
        this.font = font;

        this.foreground = foreground;
        this.background = background;

        this.dimension = dimension;
        this.listener = listener;

        add(createButton());
        this.setBackground(background);
    }

    private JButton createButton() {
        JButton button = new JButton(this.text);

        button.setFont(this.font);

        button.setForeground(this.foreground);
        button.setBackground(this.background);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setMinimumSize(this.dimension);
        button.setPreferredSize(this.dimension);
        button.setMaximumSize(this.dimension);

        button.setBorderPainted(false);
        button.setFocusPainted(false);

        if (this.listener != null)
            button.addActionListener(this.listener);

        return button;
    }
}
