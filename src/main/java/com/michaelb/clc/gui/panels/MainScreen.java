package com.michaelb.clc.gui.panels;

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

import com.michaelb.clc.gui.Frame;
import com.michaelb.clc.gui.components.Button.ButtonBuilder;;

public class MainScreen extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.BLACK;

    private Frame context;

    public MainScreen(Frame context) {
        this.context = context;
        this.configurePanel();
        this.addButtons();
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
    public void paintComponent(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;

        g2D.setColor(BACKGROUND_COLOR);
        g2D.fillRect(0, 0, this.getWidth(), this.getHeight());

        //  TODO: star fade-in-out animation - opacity?
    }
}
