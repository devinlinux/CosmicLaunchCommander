package com.michaelb.clc.gui.panels.main;

//  imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.components.Button.ButtonBuilder;

public class MainScreen extends JPanel implements ComponentListener {

    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private static final int NUM_STARS = 256;
    private static final int STAR_SIZE = 2;
    private static final int FADE_DURATION = 50;

    private final Frame context;
    private final List<Star> stars;
    private final Random rand;

    public MainScreen(final Frame context) {
        this.context = context;
        this.stars = new ArrayList<>(NUM_STARS);
        this.rand = new Random();

        initStars();

        this.configurePanel();
        this.addButtons();

        startAnimation();
    }

    private void initStars() {
        for (int i = 0; i < NUM_STARS; i++) {
            int x = rand.nextInt(context.getWidth() - STAR_SIZE * 2) + STAR_SIZE;
            int y = rand.nextInt(context.getHeight() - STAR_SIZE * 2) + STAR_SIZE;

            this.stars.add(new Star(x, y, STAR_SIZE, FADE_DURATION));
        }
    }

    private void reinitStars() {
        for (Star star : this.stars) {
            star.x = rand.nextInt(context.getWidth() - STAR_SIZE * 2) + STAR_SIZE;
            star.y = rand.nextInt(context.getHeight() - STAR_SIZE * 2) + STAR_SIZE;
        }
    }

    private void startAnimation() {
        new Thread(() -> {
            while (true) {
                for (Star star : stars)
                    star.update();
                repaint();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.err.printf("Error sleeping for star animaiton thread: %s%n", e.getMessage());
                }
            }
        }).start();
    }

    private void configurePanel() {
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setLayout(new GridBagLayout());
    }

    private void addButtons() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttons = new JPanel(new GridBagLayout());

        buttons.add(new ButtonBuilder()
                .withText("Construct New Rocket")
                .withHoverColor(Color.DARK_GRAY)
                .build(), gbc);

        buttons.add(new ButtonBuilder()
                .withText("Launch Rocket")
                .withHoverColor(Color.DARK_GRAY)
                .build(), gbc);

        gbc.weighty = 1;
        add(buttons, gbc);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(BACKGROUND_COLOR);
        g2D.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (Star star : this.stars)
            star.draw(g);
    }

    /* component listener */

    @Override
    public void componentResized(ComponentEvent e) {
        initStars();
    }

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}
}
