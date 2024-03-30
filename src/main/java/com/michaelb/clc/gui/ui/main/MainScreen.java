package com.michaelb.clc.gui.ui.main;

//  imports
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.Stage;
import com.michaelb.clc.gui.components.Button.ButtonBuilder;
import com.michaelb.clc.util.Logger;

public class MainScreen extends JPanel {

    private static final String TITLE = "COSMIC LAUNCH COMMANDER";
    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private static final int NUM_STARS = 256;
    private static final int STAR_SIZE = 2;
    private static final int FADE_DURATION = 50;

    private final Frame context;
    private final Random rand;

    private final List<Star2D> stars;

    private final GridBagConstraints gbc;

    public MainScreen(final Frame context) {
        this.context = context;
        this.stars = new ArrayList<>(NUM_STARS);
        this.rand = new Random();
        this.gbc = new GridBagConstraints();

        initStars();

        this.configurePanel();
        this.addTitle();
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
                    Logger.err("Error sleeping for star animation thread: %s".formatted(e.getMessage()), "MainScreen::startAnimation");
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

    private void addTitle() {
        this.gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.gbc.anchor = GridBagConstraints.NORTH;

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Nasalization Rg", Font.PLAIN, 74));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);

        titlePanel.add(title);
        this.add(titlePanel, this.gbc);
    }

    private void addButtons() {
        this.gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.gbc.anchor = GridBagConstraints.CENTER;
        this.gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttons = new JPanel(new GridBagLayout());
        buttons.setOpaque(false);

        buttons.add(new ButtonBuilder()
                .withText("Construct New Rocket")
                .withHoverColor(Color.DARK_GRAY)
                .withActionListener(_ -> context.stage(Stage.CONSTRUCTION))
                .build(), this.gbc);

        buttons.add(new ButtonBuilder()
                .withText("Launch Rocket")
                .withHoverColor(Color.DARK_GRAY)
                .withActionListener(_ -> context.stage(Stage.LAUNCH))
                .build(), this.gbc);

        buttons.add(new ButtonBuilder()
                .withText("Manage Complex")
                .withHoverColor(Color.DARK_GRAY)
                .withActionListener(_ -> context.stage(Stage.COMPLEX))
                .build(), this.gbc);

        buttons.add(new ButtonBuilder()
                .withText("Mission Control")
                .withHoverColor(Color.DARK_GRAY)
                .withActionListener(_ -> context.stage(Stage.MISSION_CONTROL))
                .build(), this.gbc);

        gbc.weighty = 1;
        this.add(buttons, this.gbc);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        for (Star2D star : this.stars)
            star.draw(g);
    }
}
