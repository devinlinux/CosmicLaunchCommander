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

    private static final Font DEFAULT_FONT = new JButton().getFont();

    private static final Color DEFAULT_FOREGROUND = Color.GREEN;
    private static final Color DEFAULT_BACKGROUND = Color.BLACK;

    private static final Dimension DEFAULT_DIMENSION = new Dimension(250, 50);

    private String text;
    private Font font;

    private Color foreground;
    private Color background;

    private Dimension dimension;

    private JButton button;

    private Button(String text, Font font, Color foreground, Color background, Dimension dimension) {
        this.text = text;
        this.font = font;

        this.foreground = foreground;
        this.background = background;

        this.dimension = dimension;

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

        return button;
    }

    private void addHoverEffect(Color color) {
        this.button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(background);
            }
        });
    }

    public static final class ButtonBuilder {

        private static int n = 0;

        private Button button;

        public ButtonBuilder() {
            this.button = new Button(String.valueOf(n), DEFAULT_FONT, DEFAULT_FOREGROUND, DEFAULT_BACKGROUND, DEFAULT_DIMENSION);
            this.button.text = String.valueOf(n++);
        }

        public ButtonBuilder withText(String text) {
            this.button.text = text;
            return this;
        }

        public ButtonBuilder withFont(Font font) {
            this.button.font = font;
            return this;
        }

        public ButtonBuilder withForeground(Color foreground) {
            this.button.foreground = foreground;
            return this;
        }

        public ButtonBuilder withBackground(Color background) {
            this.button.background = background;
            return this;
        }

        public ButtonBuilder withHoverColor(Color color) {
            this.button.addHoverEffect(color);
            return this;
        }

        public ButtonBuilder withDimension(Dimension dimension) {
            this.button.dimension = dimension;
            return this;
        }

        public ButtonBuilder withActionListener(ActionListener listener) {
            this.button.button.addActionListener(listener);
            return this;
        }

        public JPanel build() { return this.button; }
    }
}
