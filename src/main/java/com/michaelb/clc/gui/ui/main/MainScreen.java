package com.michaelb.clc.gui.ui.main;

//  imports
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.components.Button.ButtonBuilder;

public class MainScreen extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private static final int NUM_STARS = 256;
    private static final int STAR_SIZE = 2;
    private static final int FADE_DURATION = 50;

    private final Frame context;
    private final Random rand;

    private final List<Star2D> stars;

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
            this.stars.add(new Star2D(this.randX(), this.randY(),  STAR_SIZE, FADE_DURATION));
        }
    }

    private void reinitStars() {
        for (Star2D star : this.stars) {
            star.x = this.randX();
            star.y = this.randY();
        }
    }

    private int randX() {
        return rand.nextInt(this.context.getWidth() - STAR_SIZE * 2) + STAR_SIZE;
    }

    private int randY() {
        return rand.nextInt(this.context.getHeight() - STAR_SIZE * 2) + STAR_SIZE;
    }

    private void startAnimation() {
        new Thread(() -> {
            while (true) {
                for (Star2D star : stars)
                    star.update();
                repaint();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.err.printf("Error sleeping for star animation thread: %s%n", e.getMessage());
                }
            }
        }).start();
    }

    private void configurePanel() {
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setLayout(new GridBagLayout());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reinitStars();
            }
        });
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

        buttons.add(new ButtonBuilder()
                .withText("Manage Complex")
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

        for (Star2D star : this.stars)
            star.draw(g);
    }
}
