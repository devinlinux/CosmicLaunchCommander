package com.michaelb.clc.gui.components;

//  imports
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JPanel {

    private String text;
    private Font font;

    private Color foreground;
    private Color background;
    private Color hoverColor;

    private Dimension dimension;
    private ActionListener listener;

    private Button(String text, Font font, Color foreground, Color background, Color hoverColor, Dimension dimension, ActionListener listener) {
        this.text = text;
        this.font = font;

        this.foreground = foreground;
        this.background = background;
        this.hoverColor = hoverColor;

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

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(background);
            }
        });

        return button;
    }

    public static final class ButtonBuilder {

        private String text;
        private Font font;

        private Color foreground;
        private Color background;
        private Color hoverColor;

        private Dimension dimension;
        private ActionListener listener;

        public ButtonBuilder() {
            this.text = "";
            this.font = new JButton().getFont();

            this.foreground = Color.GREEN;
            this.background = Color.BLACK;
            this.hoverColor = Color.BLACK;

            this.dimension = new Dimension(250, 50);
            this.listener = null;
        }

        public ButtonBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public ButtonBuilder withFont(Font font) {
            this.font = font;
            return this;
        }

        public ButtonBuilder withForeground(Color foreground) {
            this.foreground = foreground;
            return this;
        }

        public ButtonBuilder withBackground(Color background) {
            this.background = background;
            return this;
        }

        public ButtonBuilder withHoverColor(Color color) {
            this.hoverColor = color;
            return this;
        }

        public ButtonBuilder withDimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public ButtonBuilder withActionListener(ActionListener listener) {
            this.listener = listener;
            return this;
        }

        public JPanel build() {
            return new Button(
                    this.text,
                    this.font,
                    this.foreground,
                    this.background,
                    this.hoverColor,
                    this.dimension,
                    this.listener);
        }
    }
}
