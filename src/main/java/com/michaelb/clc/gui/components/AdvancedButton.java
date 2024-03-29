package com.michaelb.clc.gui.components;

//  imports
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class AdvancedButton extends JPanel implements MouseListener {

    private final String text;
    private final Font font;

    private final Color foreground;
    private final Color background;
    private final PanelAnimation defaultAnimation;
    private PanelAnimation hoverAnimation;
    private final PanelAnimation hoverAnimationTemp;

    private final Dimension dimension;

    private final ActionListener listener;

    private boolean hovered;

    public AdvancedButton(final String text, final Font font, final Color foreground,
            final Color background, final PanelAnimation defaultAnimation, final PanelAnimation hoverAnimation,
            final Dimension dimension, final ActionListener listener) {
        this.text = text;
        this.font = font;

        this.foreground = foreground;
        this.background = background;
        this.defaultAnimation = defaultAnimation;
        this.hoverAnimation = hoverAnimation;
        this.hoverAnimationTemp = hoverAnimation;

        this.dimension = dimension;

        this.listener = listener;

        this.hovered = false;

        createButton();
    }

    private void createButton() {
        this.setLayout(new GridBagLayout());

        this.setMinimumSize(this.dimension);
        this.setPreferredSize(this.dimension);
        this.setMaximumSize(this.dimension);

        JLabel buttonText = new JLabel(this.text);
        buttonText.setFont(this.font);
        buttonText.setForeground(this.foreground);
        buttonText.setOpaque(false);

        this.add(buttonText);
        this.setVisible(true);

        this.addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(this.background);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.hovered)
            this.hoverAnimation.draw(g, 5, 0, this.getWidth(), this.getHeight());
        else
            this.defaultAnimation.draw(g, 5, 0, this.getWidth(), this.getHeight());
    }

    @FunctionalInterface
    public interface PanelAnimation {
        void draw(final Graphics g, final int x, final int y, final int width, final int height);
    }

    /* mouse listener */

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.listener != null)
            listener.actionPerformed(null);
        //  might need interface here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hovered = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hovered = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.hoverAnimation = (g, x, y, width, height) -> {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, width, height);
        };
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.hoverAnimation = hoverAnimationTemp;
        repaint();
    }

    public static final class AdvancedButtonBuilder {

        private String text;
        private Font font;

        private Color foreground;
        private Color background;
        private PanelAnimation defaultAnimation;
        private PanelAnimation hoverAnimation;

        private Dimension dimension;
        private ActionListener listener;

        public AdvancedButtonBuilder() {
            this.text = "";
            this.font = new JLabel().getFont();

            this.foreground = Color.GREEN;
            this.background = Color.BLACK;
            this.defaultAnimation = (g, x, y, width, height) -> {
                g.setColor(Color.BLACK);
                g.fillRect(x, y, width, height);
            };
            this.hoverAnimation = (g, x, y, width, height) -> {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(x, y, width, height);
            };

            this.dimension = new Dimension(250, 50);
            this.listener = null;
        }

        public AdvancedButtonBuilder withText(final String text) {
            this.text = text;
            return this;
        }

        public AdvancedButtonBuilder withFont(final Font font) {
            this.font = font;
            return this;
        }

        public AdvancedButtonBuilder withForeground(final Color foreground) {
            this.foreground = foreground;
            return this;
        }

        public AdvancedButtonBuilder withBackground(final Color background) {
            this.background = background;
            return this;
        }

        public AdvancedButtonBuilder withDefaultAnimation(final PanelAnimation defaultAnimation) {
            this.defaultAnimation = defaultAnimation;
            return this;
        }

        public AdvancedButtonBuilder withHoverAnimation(final PanelAnimation hoverAnimation) {
            this.hoverAnimation = hoverAnimation;
            return this;
        }

        public AdvancedButtonBuilder withDimension(final Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public AdvancedButtonBuilder withActionListener(final ActionListener listener) {
            this.listener = listener;
            return this;
        }

        public JPanel build() {
            return new AdvancedButton(this.text, this.font, this.foreground, this.background,
                    this.defaultAnimation, this.hoverAnimation, this.dimension, this.listener);
        }
    }
}
