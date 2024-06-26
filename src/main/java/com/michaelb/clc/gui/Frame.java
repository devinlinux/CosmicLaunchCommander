package com.michaelb.clc.gui;

//  imports
import javax.swing.JFrame;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.Component;

import com.michaelb.clc.util.Logger;
import com.michaelb.clc.util.ProgramInfo;

import com.michaelb.clc.gui.ui.splash.SplashScreen;
import com.michaelb.clc.gui.ui.main.MainScreen;
import com.michaelb.clc.gui.ui.launch.LaunchScreen;
import com.michaelb.clc.gui.ui.construction.ConstructionScreen;
import com.michaelb.clc.gui.ui.complex.ComplexScreen;
import com.michaelb.clc.gui.ui.missionctrl.MissionControlScreen;

import static com.michaelb.clc.util.IOUtils.IMAGE_ICON_PATH;

public class Frame extends JFrame {

    private static final String LONG_TITLE = "Cosmic Launch Commander %s".formatted(ProgramInfo.VERSION);
    private static final String SHORT_TITLE = "CLC %s".formatted(ProgramInfo.VERSION);

    private static final int INIT_WIDTH = 1700;
    private static final int INIT_HEIGHT = 1000;

    private static final ImageIcon ICON = new ImageIcon(IMAGE_ICON_PATH);

    private Stage stage;

    public Frame() {
        this(Stage.SPLASH_SCREEN);
    }

    public Frame(final Stage stage) {
        super(LONG_TITLE);
        this.stage = stage;
        setup();
        display();

        Logger.info("Created new Frame: %s".formatted(super.getTitle()), "Frame::new");
    }

    private void setup() {
        this.setTitle(LONG_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(INIT_WIDTH, INIT_HEIGHT);
        this.setIconImage(ICON.getImage());
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setTitle(getWidth() <= 300 ? SHORT_TITLE : LONG_TITLE);
            }
        });
        this.setVisible(true);
    }

    private void display() {
        switch (this.stage) {
            case SPLASH_SCREEN -> this.add(new SplashScreen(this), BorderLayout.CENTER);
            case LOGIN -> System.out.println("add(new LoginScreen())");
            case MAIN -> this.add(new MainScreen(this), BorderLayout.CENTER);
            case CONSTRUCTION -> this.add(new ConstructionScreen(this), BorderLayout.CENTER);
            case LAUNCH -> this.add(new LaunchScreen(this), BorderLayout.CENTER);
            case COMPLEX -> this.add(new ComplexScreen(this), BorderLayout.CENTER);
            case MISSION_CONTROL -> this.add(new MissionControlScreen(this), BorderLayout.CENTER);
        }
        setVisible(true);
    }

    public void stage(final Stage stage) {
        this.stage = stage;

        clearComponents();
        display();
    }

    private void clearComponents() {
        for (Component component : this.getContentPane().getComponents())
            this.getContentPane().remove(component);

        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }
}
