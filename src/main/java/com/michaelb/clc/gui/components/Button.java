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

public final class Button extends JPanel {

    private final String text;
    private final Font font;

    private final Color foreground;
    private final Color background;
    private final Color hoverColor;

    private final boolean transparent;

    private final Dimension dimension;
    private final ActionListener listener;

    private Button(final String text, final Font font, final Color foreground, final Color background,
                   final Color hoverColor, final boolean transparent, final Dimension dimension,
                   final ActionListener listener) {
        this.text = text;
        this.font = font;

        this.foreground = foreground;
        this.background = background;
        this.hoverColor = hoverColor;

        this.transparent = transparent;

        this.dimension = dimension;
        this.listener = listener;

        add(createButton());

        if (!transparent)
            this.setBackground(background);
        else
            this.setOpaque(false);
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
            public void mouseEntered(final MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                button.setBackground(background);
            }
        });

        if (this.transparent) {
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
        }

        return button;
    }

    public static final class ButtonBuilder {

        private String text;
        private Font font;

        private Color foreground;
        private Color background;
        private Color hoverColor;

        private boolean transparent;

        private Dimension dimension;
        private ActionListener listener;

        public ButtonBuilder() {
            this.text = "";
            this.font = new JButton().getFont();

            this.foreground = Color.GREEN;
            this.background = Color.BLACK;
            this.hoverColor = Color.BLACK;

            this.transparent = false;

            this.dimension = new Dimension(250, 50);
            this.listener = null;
        }

        public ButtonBuilder withText(final String text) {
            this.text = text;
            return this;
        }

        public ButtonBuilder withFont(final Font font) {
            this.font = font;
            return this;
        }

        public ButtonBuilder withForeground(final Color foreground) {
            this.foreground = foreground;
            return this;
        }

        public ButtonBuilder withBackground(final Color background) {
            this.background = background;
            return this;
        }

        public ButtonBuilder withHoverColor(final Color color) {
            this.hoverColor = color;
            return this;
        }

        public ButtonBuilder withTransparency() {
            this.transparent = true;
            return this;
        }

        public ButtonBuilder withDimension(final Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public ButtonBuilder withActionListener(final ActionListener listener) {
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
                    this.transparent,
                    this.dimension,
                    this.listener);
        }
    }
}
